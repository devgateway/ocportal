package org.devgateway.toolkit.forms.service;

import org.apache.commons.lang3.Validate;
import org.devgateway.ocds.persistence.mongo.Amount;
import org.devgateway.ocds.persistence.mongo.Award;
import org.devgateway.ocds.persistence.mongo.Bids;
import org.devgateway.ocds.persistence.mongo.Budget;
import org.devgateway.ocds.persistence.mongo.Classification;
import org.devgateway.ocds.persistence.mongo.Contract;
import org.devgateway.ocds.persistence.mongo.Document;
import org.devgateway.ocds.persistence.mongo.Item;
import org.devgateway.ocds.persistence.mongo.MakueniPlanning;
import org.devgateway.ocds.persistence.mongo.Organization;
import org.devgateway.ocds.persistence.mongo.Planning;
import org.devgateway.ocds.persistence.mongo.Release;
import org.devgateway.ocds.persistence.mongo.Tender;
import org.devgateway.ocds.persistence.mongo.Unit;
import org.devgateway.toolkit.persistence.dao.form.AwardAcceptance;
import org.devgateway.toolkit.persistence.dao.form.AwardNotification;
import org.devgateway.toolkit.persistence.dao.form.CabinetPaper;
import org.devgateway.toolkit.persistence.dao.form.PlanItem;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.dao.form.ProfessionalOpinion;
import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisition;
import org.devgateway.toolkit.persistence.dao.form.TenderQuotationEvaluation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MakueniToOCDSConversionServiceImpl implements MakueniToOCDSConversionService {

    private static final String OCID_PREFIX = "ocds-abcd-";

    @Autowired
    private MongoFileStorageService mongoFileStorageService;

    @Override
    public Tender createTender(org.devgateway.toolkit.persistence.dao.form.Tender tender) {
        return null;
    }


    @Override
    public Budget createPlanningBudget(ProcurementPlan procurementPlan) {
        Budget budget = new Budget();
        //set project id as a comma separated list of sources of funds
        safeSet(
                budget::setProjectID,
                () -> procurementPlan.getPlanItems()
                        .stream()
                        .map(PlanItem::getSourceOfFunds)
                        .collect(Collectors.joining())
        );

        return budget;
    }

    @Override
    public MakueniPlanning createPlanning(ProcurementPlan procurementPlan, CabinetPaper paper) {
        MakueniPlanning planning = new MakueniPlanning();

        planning.setBudget(createPlanningBudget(procurementPlan));
        //set planning extension items
        procurementPlan.getPlanItems()
                .stream()
                .map(this::createPlanningItem)
                .collect(Collectors.toCollection(planning::getItems));

        planning.getDocuments().add(mongoFileStorageService.storeFileAndReferenceAsDocument(
                procurementPlan.getFormDoc(), Document.DocumentType.PROCUREMENT_PLAN));

        //TODO: planning.getDocuments().add()

        return planning;
    }


    /**
     * Will set value only if not empty, but first it converts it using converter
     *
     * @param consumer
     * @param supplier
     * @param converter
     * @param <S>
     * @param <C>
     */
    public <S, C> void safeSet(Consumer<C> consumer, Supplier<S> supplier, Function<S, C> converter) {
        S o = supplier.get();
        if (!ObjectUtils.isEmpty(o)) {
            consumer.accept(converter.apply(o));
        }
    }

    /**
     * same as #safeSet(Consumer, Supplier, Function), but with {@link Function#identity()} as converter
     *
     * @param consumer
     * @param supplier
     * @param <S>
     */
    public <S> void safeSet(Consumer<S> consumer, Supplier<S> supplier) {
        safeSet(consumer, supplier, Function.identity());
    }

    /**
     * Convertor for ids into string ids. We do not use Long::toString because method signature is not unique at compile
     * time
     *
     * @param id
     * @return
     */
    public String idToString(Long id) {
        return id.toString();
    }

    @Override
    public Unit createPlanningItemUnit(PlanItem item) {
        Unit unit = new Unit();
        unit.setScheme("scheme");
        safeSet(unit::setName, item::getUnitOfIssue);
        safeSet(unit::setId, item::getId, this::idToString);
        unit.setValue(createPlanningItemUnitAmount(item));
        return unit;
    }

    @Override
    public Amount createPlanningItemUnitAmount(PlanItem item) {
        Amount amount = new Amount();
        safeSet(amount::setAmount, item::getUnitPrice);
        amount.setCurrency(getCurrency());
        return amount;
    }

    @Override
    public Item createPlanningItem(PlanItem item) {
        Item ret = new Item();
        safeSet(ret::setId, item::getId, this::idToString);
        safeSet(ret::setDescription, item::getDescription);
        ret.setUnit(createPlanningItemUnit(item));
        safeSet(ret::setQuantity, item::getQuantity, Integer::doubleValue);
        ret.setClassification(createPlanningItemClassification(item));
        return ret;
    }

    @Override
    public Classification createPlanningItemClassification(PlanItem item) {
        Classification classification = new Classification();
        safeSet(classification::setId, item.getItem()::getCode);
        safeSet(classification::setDescription, item.getItem()::getLabel);
        return classification;
    }

    @Override
    public Amount.Currency getCurrency() {
        return Amount.Currency.KES;
    }

    @Override
    public Set<Organization> createParties(Tender tender, Planning planning) {
        return null;
    }

    @Override
    public Bids createBids(TenderQuotationEvaluation quotationEvaluation) {
        return null;
    }

    @Override
    public Award createAward(ProfessionalOpinion professionalOpinion, AwardNotification awardNotification,
                             AwardAcceptance awardAcceptance) {
        return null;
    }

    @Override
    public Contract createContract(org.devgateway.toolkit.persistence.dao.form.Contract contract) {
        return null;
    }


    public String getOcid(PurchaseRequisition purchaseRequisition) {
        Validate.notNull(purchaseRequisition.getPurchaseRequestNumber(),
                "purchaseRequestNumber must not be null!");
        return OCID_PREFIX + purchaseRequisition.getPurchaseRequestNumber();
    }

    @Override
    public Release createRelease(PurchaseRequisition purchaseRequisition) {
        Release release = new Release();
        release.setOcid(getOcid(purchaseRequisition));
        release.setPlanning(createPlanning(
                purchaseRequisition.getProcurementPlan(),
                purchaseRequisition.getProject().getCabinetPaper()
        ));
        return release;
    }
}
