package org.devgateway.toolkit.persistence.validator.validators;


import org.apache.commons.lang3.StringUtils;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.categories.Supplier;
import org.devgateway.toolkit.persistence.dao.form.AbstractMakueniEntity;
import org.devgateway.toolkit.persistence.dao.form.AwardAcceptance;
import org.devgateway.toolkit.persistence.dao.form.AwardAcceptanceItem;
import org.devgateway.toolkit.persistence.dao.form.AwardNotificationItem;
import org.devgateway.toolkit.persistence.dao.form.Bid;
import org.devgateway.toolkit.persistence.dao.form.Contract;
import org.devgateway.toolkit.persistence.dao.form.PaymentVoucher;
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
import java.util.function.Function;
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
        return TenderProcess.class.isAssignableFrom(clazz);
    }

    public static <E extends AbstractMakueniEntity> boolean existsNonDraft(E e) {
        return e != null && !DBConstants.Status.TERMINATED.equals(e.getStatus())
                && !DBConstants.Status.DRAFT.equals(e.getStatus());
    }

    public static <E extends AbstractMakueniEntity> Stream<E> nonDraft(Collection<E> col) {
        return col.stream().filter(TenderProcessValidator::existsNonDraft);
    }

    public static <E extends AbstractMakueniEntity> Stream<E> draft(Collection<E> col) {
        return col.stream().filter(TenderProcessValidator::existsDraft);
    }

    public static <E extends AbstractMakueniEntity> Boolean existsDraft(Collection<E> col) {
        return col.stream().anyMatch(TenderProcessValidator::existsDraft);
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
                    .filter(Objects::nonNull).collect(Collectors.toSet());
            suppliersMap.put("the Passed Bidders from Quotation Evaluation Form", qaPassedSuppliers);
        }

        if (existsNonDraft(tp.getProfessionalOpinion())) {
            Set<Supplier> piAwardees = nonDraft(tp.getProfessionalOpinion()).flatMap(p -> p.getItems().stream())
                    .map(ProfessionalOpinionItem::getAwardee).
                            filter(Objects::nonNull).collect(Collectors.toSet());
            suppliersMap.put("the Awardees from Professional Opinion Form", piAwardees);
        }

        if (existsNonDraft(tp.getAwardNotification())) {
            Set<Supplier> anAwardee = nonDraft(tp.getAwardNotification())
                    .flatMap(a -> a.getAwardee().stream()).filter(Objects::nonNull).collect(Collectors.toSet());
            suppliersMap.put("the Suppliers from at least one Award Notification Form", anAwardee);
        }

        if (existsNonDraft(tp.getAwardAcceptance())) {
            Set<Supplier> aaAwardees = nonDraft(tp.getAwardAcceptance()).flatMap(a -> a.getAwardee().stream())
                    .filter(Objects::nonNull).collect(Collectors.toSet());
            suppliersMap.put("the Suppliers from at least one Award Acceptance Form", aaAwardees);

            Set<Supplier> aacAwardees = nonDraft(tp.getAwardAcceptance()).map(AwardAcceptance::getAcceptedAcceptance)
                    .filter(Objects::nonNull).map(AwardAcceptanceItem::getAwardee).
                            filter(Objects::nonNull).collect(Collectors.toSet());
            suppliersMap.put("the Accepted Suppliers from at least one Award Acceptance Form", aacAwardees);
        }

        if (existsNonDraft(tp.getAwardAcceptance())) {
            Set<Supplier> contractSuppliers = nonDraft(tp.getContract()).map(Contract::getAwardee)
                    .filter(Objects::nonNull).collect(Collectors.toSet());
            suppliersMap.put("the Contract Suppliers from the Contract Form", contractSuppliers);
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
                StringUtils.capitalize(friendlyJoin(value) + " do not match " + friendlyJoin(key))));
    }

    public static String friendlyJoin(Set<String> set) {
        ArrayList<String> elements = new ArrayList<>(set);
        List<String> tmp = new ArrayList<>(elements);
        tmp.removeAll(Collections.singleton(null)); //Remove all nulled items

        int size = tmp.size();
        return size == 0 ? "" : size == 1 ? tmp.get(0) : String.join(", ", tmp.subList(0, --size))
                .concat(" and ").concat(tmp.get(size));
    }

    public List<AbstractMakueniEntity> createAllFormsList(TenderProcess tp) {
        List<AbstractMakueniEntity> allForms = new ArrayList<>();
        allForms.add(tp.getProcurementPlan());
        allForms.add(tp.getProject());
        allForms.add(tp);
        allForms.add(tp.getSingleTender());
        allForms.add(tp.getSingleTenderQuotationEvaluation());
        allForms.add(tp.getSingleProfessionalOpinion());
        allForms.add(tp.getSingleAwardNotification());
        allForms.add(tp.getSingleAwardAcceptance());
        allForms.add(tp.getSingleContract());
        allForms.addAll(tp.getAdministratorReports());
        allForms.addAll(tp.getInspectionReports());
        allForms.addAll(tp.getPmcReports());
        allForms.addAll(tp.getMeReports());
        allForms.addAll(tp.getPaymentVouchers());
        return allForms;
    }

    public boolean existsLowerFormsInDraft(List<AbstractMakueniEntity> allForms, AbstractMakueniEntity f) {
        int i = allForms.indexOf(f);
        for (int x = 0; x < allForms.size(); x++) {
            if (x <= i) {
                continue;
            }
            if (existsNonDraft(allForms.get(x))) {
                return true;
            }
        }
        return false;
    }

    public boolean existsDraftWithLowerFormsDraft(List<AbstractMakueniEntity> allForms, AbstractMakueniEntity f) {
        return existsDraft(f) && existsLowerFormsInDraft(allForms, f);
    }

    public <E extends AbstractMakueniEntity> boolean draftLinkedPaymentVouchers(Set<PaymentVoucher> paymentVouchers,
                                                                                Set<E> linkedSet,
                                                                                Function<? super PaymentVoucher,
                                                                                        ? extends E>
                                                                                        linkedGetter) {
        return existsDraft(linkedSet) && nonDraft(paymentVouchers)
                .map(linkedGetter).filter(Objects::nonNull).anyMatch(r -> draft(linkedSet).anyMatch(d -> d.equals(r)));
    }

    public void reportDraft(TenderProcess tp, Errors errors) {
        List<AbstractMakueniEntity> allForms = createAllFormsList(tp);
        ArrayList<String> draftForms = new ArrayList<>();
        if (existsDraftWithLowerFormsDraft(allForms, tp.getProcurementPlan())) {
            draftForms.add("Procurement Plan");
        }
        if (existsDraftWithLowerFormsDraft(allForms, tp.getProject())) {
            draftForms.add("Project");
        }

        if (existsDraftWithLowerFormsDraft(allForms, tp)) {
            draftForms.add("Purchase Requisition");
        }

        if (existsDraftWithLowerFormsDraft(allForms, tp.getSingleTender())) {
            draftForms.add("Tender");
        }

        if (existsDraftWithLowerFormsDraft(allForms, tp.getSingleTenderQuotationEvaluation())) {
            draftForms.add("Quotation and Evaluation");
        }

        if (existsDraftWithLowerFormsDraft(allForms, tp.getSingleProfessionalOpinion())) {
            draftForms.add("Professional Opinion");
        }

        if (existsDraftWithLowerFormsDraft(allForms, tp.getSingleAwardNotification())) {
            draftForms.add("Award Notification");
        }

        if (existsDraftWithLowerFormsDraft(allForms, tp.getSingleAwardAcceptance())) {
            draftForms.add("Award Acceptance");
        }

        if (existsDraftWithLowerFormsDraft(allForms, tp.getSingleContract())) {
            draftForms.add("Contract");
        }

        if (draftLinkedPaymentVouchers(tp.getPaymentVouchers(), tp.getAdministratorReports(),
                PaymentVoucher::getAdministratorReport)) {
            draftForms.add("Administrator Report");
        }

        if (draftLinkedPaymentVouchers(tp.getPaymentVouchers(), tp.getInspectionReports(),
                PaymentVoucher::getInspectionReport)) {
            draftForms.add("Inspection Report");
        }

        if (draftLinkedPaymentVouchers(tp.getPaymentVouchers(), tp.getPmcReports(),
                PaymentVoucher::getPmcReport)) {
            draftForms.add("PMC Report");
        }

//        if (existsDraft(tp.getMeReports())) {
//            draftForms.add("M&E Reports");
//        }

//        if (existsDraft(tp.getPaymentVouchers())) {
//            draftForms.add("Payment Vouchers");
//        }

        if (!draftForms.isEmpty()) {
            errors.reject("The following forms are in draft: " + String.join(", ", draftForms));
        }

    }

    public void validateDates(TenderProcess tp, Errors errors) {
        if (existsNonDraftPair(tp.getAwardNotification(), tp.getTender())) {
            if (tp.getAwardNotification().stream().flatMap(a -> a.getItems().stream())
                    .map(AwardNotificationItem::getAwardDate).anyMatch(
                            d -> d.before(tp.getSingleTender().getInvitationDate()))) {
                errors.reject("At least one Award Notification date is earlier than the Tender "
                        + "invitation date");
            }
        }

        if (existsNonDraftPair(tp.getAwardAcceptance(), tp.getContract())) {
            if (tp.getSingleAwardAcceptance().getAcceptedAcceptance() != null
                    && tp.getContract().stream().map(Contract::getContractDate)
                    .anyMatch(
                            d -> d.before(tp.getSingleAwardAcceptance().getAcceptedAcceptance().getAcceptanceDate()))
            ) {
                errors.reject("The Contract date is earlier than the Award Acceptance date");
            }
        }
    }

    public void validateCrossCounts(TenderProcess tp, Errors errors) {
        AtomicInteger poCount = new AtomicInteger();
        nonDraft(tp.getProfessionalOpinion()).findFirst().ifPresent(i -> poCount.set(i.getItems().size()));
        AtomicInteger anCount = new AtomicInteger();
        nonDraft(tp.getAwardNotification()).findFirst().ifPresent(i -> anCount.set(i.getItems().size()));
        AtomicInteger aaCount = new AtomicInteger();
        nonDraft(tp.getAwardAcceptance()).findFirst().ifPresent(i -> aaCount.set(i.getItems().size()));

        if (existsNonDraftPair(tp.getAwardNotification(), tp.getProfessionalOpinion())) {
            if (poCount.get() != anCount.get()) {
                errors.reject("The Award Notifications count must match the Professional Opinions count");
            }
        }

        if (existsNonDraftPair(tp.getAwardNotification(), tp.getAwardAcceptance())) {
            if (aaCount.get() != anCount.get()) {
                errors.reject("The Award Acceptance responses count must match the Award Notifications count");
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
        validateCrossCounts(tp, errors);
        validateDates(tp, errors);
    }
}
