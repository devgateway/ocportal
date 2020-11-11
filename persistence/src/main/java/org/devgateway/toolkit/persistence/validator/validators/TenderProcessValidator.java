package org.devgateway.toolkit.persistence.validator.validators;

import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.categories.Supplier;
import org.devgateway.toolkit.persistence.dao.form.AbstractMakueniEntity;
import org.devgateway.toolkit.persistence.dao.form.AwardAcceptance;
import org.devgateway.toolkit.persistence.dao.form.AwardAcceptanceItem;
import org.devgateway.toolkit.persistence.dao.form.Bid;
import org.devgateway.toolkit.persistence.dao.form.Contract;
import org.devgateway.toolkit.persistence.dao.form.ProfessionalOpinionItem;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TenderProcessValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return TenderProcess.class.equals(clazz);
    }

    public static <E extends AbstractMakueniEntity> Stream<E> nonDraft(Collection<E> col) {
        return col.stream().filter(e -> !DBConstants.Status.DRAFT.equals(e.getStatus()));
    }

    public static <E extends AbstractMakueniEntity> Boolean existsNonDraft(Collection<E> col) {
        return nonDraft(col).findFirst().isPresent();
    }

    public static <E extends AbstractMakueniEntity, F extends AbstractMakueniEntity> Boolean existsNonDraftPair(
            Collection<E> col1, Collection<F> col2) {
        return existsNonDraft(col1) && existsNonDraft(col2);
    }


    public void validateAwardeeLinks(TenderProcess tp, Errors errors) {

        LinkedHashMap<String, Set<Supplier>> suppliersMap = new LinkedHashMap<>();

        Set<Supplier> qaPassedSuppliers = nonDraft(tp.getTenderQuotationEvaluation())
                .flatMap(a -> a.getBids().stream()).filter(b -> DBConstants.SupplierResponsiveness.PASS
                        .equalsIgnoreCase(b.getSupplierResponsiveness())).map(Bid::getSupplier)
                .collect(Collectors.toSet());

        suppliersMap.put("Bidders (passed) from a submitted Quotation Evaluation Bidders Form", qaPassedSuppliers);

        Set<Supplier> piAwardees = nonDraft(tp.getProfessionalOpinion()).flatMap(p -> p.getItems().stream())
                .map(ProfessionalOpinionItem::getAwardee).collect(Collectors.toSet());

        suppliersMap.put("Awardees from a submitted Professional Opinion Form", piAwardees);

        Set<Supplier> anAwardee = nonDraft(tp.getAwardNotification())
                .flatMap(a -> a.getAwardee().stream()).collect(Collectors.toSet());

        suppliersMap.put("Suppliers from a submitted Award Notification Form", anAwardee);

        Set<Supplier> aaAwardees = nonDraft(tp.getAwardAcceptance()).flatMap(a -> a.getAwardee().stream())
                .collect(Collectors.toSet());

        suppliersMap.put("Suppliers from a submitted Award Acceptance Form", aaAwardees);

        Set<Supplier> aacAwardees = nonDraft(tp.getAwardAcceptance()).map(AwardAcceptance::getAcceptedAcceptance)
                .filter(Objects::nonNull).map(AwardAcceptanceItem::getAwardee).collect(Collectors.toSet());

        suppliersMap.put("Accepted Suppliers from a submitted Award Acceptance Form", aacAwardees);

        Set<Supplier> contractSuppliers = nonDraft(tp.getContract()).map(Contract::getAwardee)
                .collect(Collectors.toSet());

        suppliersMap.put("Contract Suppliers from a submitted Contract Form", contractSuppliers);

        Set<String> keys = suppliersMap.keySet();
        for (String key1 : keys) {
            for (String key2 : keys) {
                if (key1.equals(key2)) {
                    break;
                }
                Set<Supplier> suppliers = suppliersMap.get(key1);
                Set<Supplier> upstreamSuppliers = suppliersMap.get(key2);
                if (!upstreamSuppliers.containsAll(suppliers)) {
                    errors.reject(key1 + " not found within the " + key2);
                }
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
                errors.reject("The award notifications count must match the professional opinions count in"
                        + " the previous form!");
            }
        }
    }

    @Override
    public void validate(Object target, Errors errors) {
        TenderProcess tp = (TenderProcess) target;
        validateAwardeeLinks(tp, errors);
        validatePoCount(tp, errors);
    }
}
