package org.devgateway.toolkit.persistence.validator.validators;


import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.categories.Supplier;
import org.devgateway.toolkit.persistence.dao.form.AbstractMakueniEntity;
import org.devgateway.toolkit.persistence.dao.form.AwardAcceptance;
import org.devgateway.toolkit.persistence.dao.form.AwardAcceptanceItem;
import org.devgateway.toolkit.persistence.dao.form.AwardNotificationItem;
import org.devgateway.toolkit.persistence.dao.form.Bid;
import org.devgateway.toolkit.persistence.dao.form.Contract;
import org.devgateway.toolkit.persistence.dao.form.ProfessionalOpinionItem;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TenderProcessValidator implements Validator {

    private Boolean submission = false;

    public TenderProcessValidator(Boolean submission) {
        this.submission = submission;
    }

    public TenderProcessValidator() {
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return TenderProcess.class.equals(clazz);
    }

    public static <E extends AbstractMakueniEntity> Stream<E> nonDraft(Collection<E> col) {
        return col.stream().filter(e -> !DBConstants.Status.DRAFT.equals(e.getStatus()));
    }

    public static <E extends AbstractMakueniEntity> Boolean existsDraft(Collection<E> col) {
        return col.stream().anyMatch(e -> DBConstants.Status.DRAFT.equals(e.getStatus()));
    }

    public static <E extends AbstractMakueniEntity> Boolean isStatusNull(Collection<E> col) {
        return col.stream().map(AbstractMakueniEntity::getStatus).filter(Objects::isNull).count() != 0;
    }

    public static Boolean existsDraft(AbstractMakueniEntity e) {
        return e != null && DBConstants.Status.DRAFT.equals(e.getStatus());
    }

    public static <E extends AbstractMakueniEntity> Boolean existsNonDraft(Collection<E> col) {
        return nonDraft(col).findFirst().isPresent();
    }

    public static <E extends AbstractMakueniEntity> Boolean existsNonDraftWithNull(Collection<E> col,
                                                                                   Boolean submission) {
        if (!submission) {
            return nonDraft(col).findFirst().isPresent();
        } else {
            if (isStatusNull(col)) {
                return null;
            } else {
                return nonDraft(col).findFirst().isPresent();
            }
        }
    }

    public static <E extends AbstractMakueniEntity, F extends AbstractMakueniEntity> Boolean existsNonDraftPair(
            Collection<E> col1, Collection<F> col2) {
        return existsNonDraft(col1) && existsNonDraft(col2);
    }

    public void validateAwardeeLinks(TenderProcess tp, Errors errors) {
        LinkedHashMap<String, Set<Supplier>> suppliersMap = new LinkedHashMap<>();

        if (existsNonDraft(tp.getTenderQuotationEvaluation())) {
            Set<Supplier> qaPassedSuppliers = nonDraft(tp.getTenderQuotationEvaluation())
                    .flatMap(a -> a.getBids().stream()).filter(b -> DBConstants.SupplierResponsiveness.PASS
                            .equalsIgnoreCase(b.getSupplierResponsiveness())).map(Bid::getSupplier)
                    .collect(Collectors.toSet());
            suppliersMap.put("The Passed Bidders from Quotation Evaluation Form", qaPassedSuppliers);
        }

        if (existsNonDraft(tp.getProfessionalOpinion())) {
            Set<Supplier> piAwardees = nonDraft(tp.getProfessionalOpinion()).flatMap(p -> p.getItems().stream())
                    .map(ProfessionalOpinionItem::getAwardee).collect(Collectors.toSet());
            suppliersMap.put("The Awardees from Professional Opinion Form", piAwardees);
        }

        if (existsNonDraft(tp.getAwardNotification())) {
            Set<Supplier> anAwardee = nonDraft(tp.getAwardNotification())
                    .flatMap(a -> a.getAwardee().stream()).collect(Collectors.toSet());
            suppliersMap.put("The Suppliers from at least an Award Notification Form", anAwardee);
        }

        if (existsNonDraft(tp.getAwardAcceptance())) {
            Set<Supplier> aaAwardees = nonDraft(tp.getAwardAcceptance()).flatMap(a -> a.getAwardee().stream())
                    .collect(Collectors.toSet());
            suppliersMap.put("The Suppliers from at least an Award Acceptance Form", aaAwardees);

            Set<Supplier> aacAwardees = nonDraft(tp.getAwardAcceptance()).map(AwardAcceptance::getAcceptedAcceptance)
                    .filter(Objects::nonNull).map(AwardAcceptanceItem::getAwardee).collect(Collectors.toSet());
            suppliersMap.put("The Accepted Suppliers from at least an Award Acceptance Form", aacAwardees);
        }

        if (existsNonDraft(tp.getAwardAcceptance())) {
            Set<Supplier> contractSuppliers = nonDraft(tp.getContract()).map(Contract::getAwardee)
                    .collect(Collectors.toSet());
            suppliersMap.put("The Contract Suppliers from the Contract Form", contractSuppliers);
        }

        LinkedHashMap<String, Set<String>> foundErrors = new LinkedHashMap<>();

        Set<String> keys = suppliersMap.keySet();
        for (String key1 : keys) {
            for (String key2 : keys) {
                if (key1.equals(key2)) {
                    break;
                }
                Set<Supplier> suppliers = suppliersMap.get(key1);
                Set<Supplier> upstreamSuppliers = suppliersMap.get(key2);

                if (!upstreamSuppliers.containsAll(suppliers)) {
                    if (foundErrors.containsKey(key2)) {
                        foundErrors.get(key2).add(key1);
                    } else {
                        foundErrors.put(key2, new HashSet<>(Arrays.asList(key1)));
                    }
                }
            }
        }

        Map<Set<String>, Set<String>> groupedErrors = foundErrors.entrySet().stream()
                .collect(Collectors.groupingBy(Map.Entry::getValue, Collectors.mapping(Map.Entry::getKey,
                        Collectors.toSet())));

        groupedErrors.forEach((key, value) -> errors.reject(
                friendlyJoin(value) + " do not match " + friendlyJoin(key)));
    }

    public static String friendlyJoin(Set<String> set) {
        ArrayList<String> elements = new ArrayList<>(set);
        List<String> tmp = new ArrayList<>(elements);
        tmp.removeAll(Collections.singleton(null)); //Remove all nulled items

        int size = tmp.size();
        return size == 0 ? "" : size == 1 ? tmp.get(0) : String.join(", ", tmp.subList(0, --size))
                .concat(" and ").concat(tmp.get(size));
    }

    public void reportDraft(TenderProcess tp, Errors errors) {
        ArrayList<String> draftForms = new ArrayList<>();
        if (existsDraft(tp.getProcurementPlan())) {
            draftForms.add("Procurement Plan");
        }
        if (existsDraft(tp.getProject())) {
            draftForms.add("Project");
        }

        if (existsDraft(tp)) {
            draftForms.add("Purchase Requisition");
        }

        if (existsDraft(tp.getTender())) {
            draftForms.add("Tender");
        }

        if (existsDraft(tp.getTenderQuotationEvaluation())) {
            draftForms.add("Quotation and Evaluation");
        }

        if (existsDraft(tp.getProfessionalOpinion())) {
            draftForms.add("Professional Opinion");
        }

        if (existsDraft(tp.getAwardNotification())) {
            draftForms.add("Award Notification");
        }

        if (existsDraft(tp.getAwardAcceptance())) {
            draftForms.add("Award Acceptance");
        }

        if (existsDraft(tp.getContract())) {
            draftForms.add("Contract");
        }

        if (existsDraft(tp.getAdministratorReports())) {
            draftForms.add("Administrator Reports");
        }

        if (existsDraft(tp.getInspectionReports())) {
            draftForms.add("Inspection Reports");
        }

        if (existsDraft(tp.getPmcReports())) {
            draftForms.add("PMC Reports");
        }

        if (existsDraft(tp.getMeReports())) {
            draftForms.add("M&E Reports");
        }

        if (existsDraft(tp.getPaymentVouchers())) {
            draftForms.add("Payment Vouchers");
        }

        if (!draftForms.isEmpty()) {
            errors.reject("The following forms are in draft: " + String.join(", ", draftForms));
        }

    }

    public void validateDates(TenderProcess tp, Errors errors) {
        if (existsNonDraftPair(tp.getAwardNotification(), tp.getTender())) {
            if (tp.getAwardNotification().stream().flatMap(a -> a.getItems().stream())
                    .map(AwardNotificationItem::getAwardDate).anyMatch(
                            d -> d.before(tp.getSingleTender().getInvitationDate()))) {
                errors.reject("At least one Award Notification date is earlier than the tender "
                        + "invitation date");
            }
        }

        if (existsNonDraftPair(tp.getAwardAcceptance(), tp.getContract())) {
            if (tp.getSingleAwardAcceptance().getAcceptedAcceptance() != null
                    && tp.getContract().stream().map(Contract::getContractDate)
                    .anyMatch(
                            d -> d.before(tp.getSingleAwardAcceptance().getAcceptedAcceptance().getAcceptanceDate()))
            ) {
                errors.reject("The contract date is earlier the Award Acceptance date");
            }
        }
    }

    public void validatePoCount(TenderProcess tp, Errors errors) {
        AtomicInteger poCount = new AtomicInteger();
        nonDraft(tp.getProfessionalOpinion()).findFirst().ifPresent(i -> poCount.set(i.getItems().size()));
        AtomicInteger anCount = new AtomicInteger();
        nonDraft(tp.getAwardNotification()).findFirst().ifPresent(i -> anCount.set(i.getItems().size()));

        if (existsNonDraftPair(tp.getAwardNotification(), tp.getProfessionalOpinion())) {
            if (poCount.get() != anCount.get()) {
                errors.reject("The Award Notifications count must match the Professional Opinions count");
            }
        }
    }

    @Override
    public void validate(Object target, Errors errors) {
        TenderProcess tp = (TenderProcess) target;
        if (!submission) {
            reportDraft(tp, errors);
        }
        validateAwardeeLinks(tp, errors);
        validatePoCount(tp, errors);
        validateDates(tp, errors);
    }
}
