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
import org.devgateway.ocds.persistence.mongo.Milestone;
import org.devgateway.ocds.persistence.mongo.Organization;
import org.devgateway.ocds.persistence.mongo.Planning;
import org.devgateway.ocds.persistence.mongo.Release;
import org.devgateway.ocds.persistence.mongo.Tender;
import org.devgateway.ocds.persistence.mongo.Unit;
import org.devgateway.toolkit.persistence.dao.FileMetadata;
import org.devgateway.toolkit.persistence.dao.form.AwardAcceptance;
import org.devgateway.toolkit.persistence.dao.form.AwardNotification;
import org.devgateway.toolkit.persistence.dao.form.PlanItem;
import org.devgateway.toolkit.persistence.dao.form.ProfessionalOpinion;
import org.devgateway.toolkit.persistence.dao.form.PurchaseItem;
import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisition;
import org.devgateway.toolkit.persistence.dao.form.TenderQuotationEvaluation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

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
    public Budget createPlanningBudget(PurchaseRequisition purchaseRequisition) {
        Budget budget = new Budget();

        safeSet(budget::setProjectID, purchaseRequisition.getProject()::getProjectTitle);

//        //set project id as a comma separated list of sources of funds
//        safeSet(
//                budget::setProjectID,
//                () -> procurementPlan.getPlanItems()
//                        .stream()
//                        .map(PlanItem::getSourceOfFunds)
//                        .collect(Collectors.joining())
//        );

        return budget;
    }

    public Document convertDocToProcurementPlanType(FileMetadata fm) {
        return mongoFileStorageService.storeFileAndReferenceAsDocument(fm, Document.DocumentType.PROCUREMENT_PLAN);
    }

    @Override
    public MakueniPlanning createPlanning(PurchaseRequisition purchaseRequisition) {
        MakueniPlanning planning = new MakueniPlanning();

        safeSet(planning::setBudget, () -> purchaseRequisition, this::createPlanningBudget);

        //TODO: set planning extension items
        safeSetEach(planning.getItems()::add, purchaseRequisition::getPurchaseItems, this::createPlanningItem);

        safeSet(planning.getDocuments()::add, purchaseRequisition.getProcurementPlan()::getFormDoc,
                this::convertDocToProcurementPlanType);

        //TODO: planning.getDocuments().add()
        safeSet(planning.getMilestones()::add, () -> purchaseRequisition, this::convertPlanningMilestone);

        return planning;
    }


    @Override
    public Milestone convertPlanningMilestone(PurchaseRequisition purchaseRequisition) {
        Milestone milestone = new Milestone();
        safeSet(milestone::setType, () -> Milestone.MilestoneType.PRE_PROCUREMENT, Milestone.MilestoneType::toValue);
        safeSet(milestone::setCode, () -> "approvedDate");
        safeSet(milestone::setDateMet, purchaseRequisition::getApprovedDate);
        return milestone;
    }

    public <C, S, R extends Supplier<S>> Supplier<S> getSupplier(Supplier<C> parentSupplier, Function<C, R> childSupplier) {
        C c = parentSupplier.get();

        if (!ObjectUtils.isEmpty(c)) {
            return childSupplier.apply(c);
        }
        return null;
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
        if (supplier == null || consumer == null || converter == null) {
            return;
        }
        S o = supplier.get();
        if (!ObjectUtils.isEmpty(o)) {
            consumer.accept(converter.apply(o));
        }
    }

    public <S, C> void safeSetEach(Consumer<C> consumer, Supplier<Collection<S>> supplier, Function<S, C> converter) {
        if (supplier == null || consumer == null || converter == null) {
            return;
        }

        Collection<S> o = supplier.get();
        if (!ObjectUtils.isEmpty(o)) {
            o.stream().map(converter).filter(Objects::nonNull).forEach(consumer);
        }
    }

    public <S> void safeSetEach(Consumer<S> consumer, Supplier<Collection<S>> supplier) {
        safeSetEach(consumer, supplier, Function.identity());
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
     * Converter for ids into string ids. We do not use Long::toString because method signature is not unique at compile
     * time
     *
     * @param id
     * @return
     */
    public String longIdToString(Long id) {
        return id.toString();
    }

    @Override
    public Unit createPlanningItemUnit(PurchaseItem purchaseItem) {
        Unit unit = new Unit();
        safeSet(unit::setScheme, () -> "scheme");
        safeSet(unit::setName, purchaseItem::getUnit);
        safeSet(unit::setId, purchaseItem::getId, this::longIdToString);
        safeSet(unit::setValue, () -> purchaseItem, this::createPlanningItemUnitAmount);
        return unit;
    }

    @Override
    public Amount createPlanningItemUnitAmount(PurchaseItem purchaseItem) {
        Amount amount = new Amount();
        safeSet(amount::setAmount, purchaseItem::getAmount);
        safeSet(amount::setCurrency, this::getCurrency);
        return amount;
    }

    @Override
    public Item createPlanningItem(PurchaseItem purchaseItem) {
        Item ocdsItem = new Item();
        safeSet(ocdsItem::setId, purchaseItem::getId, this::longIdToString);
        safeSet(ocdsItem::setDescription, purchaseItem::getLabel);
        safeSet(ocdsItem::setUnit, () -> purchaseItem, this::createPlanningItemUnit);
        safeSet(ocdsItem::setQuantity, purchaseItem::getQuantity, Integer::doubleValue);
        safeSet(ocdsItem::setClassification, () -> purchaseItem, this::createPlanningItemClassification);

        return ocdsItem;
    }

    @Override
    public Classification createPlanningItemClassification(PurchaseItem purchaseItem) {

        getSupplier(purchaseItem::getPlanItem, )
        Classification classification = new Classification();
        safeSet(classification::setId, purchaseItem.getPlanItem().getItem()::getCode);
        safeSet(classification::setDescription, purchaseItem.getPlanItem().getItem()::getLabel);
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
        Validate.notNull(
                purchaseRequisition.getPurchaseRequestNumber(),
                "purchaseRequestNumber must not be null!"
        );
        return OCID_PREFIX + purchaseRequisition.getPurchaseRequestNumber();
    }

    @Override
    public Release createRelease(PurchaseRequisition purchaseRequisition) {
        Release release = new Release();
        safeSet(release::setOcid, () -> purchaseRequisition, this::getOcid);
        safeSet(release::setPlanning, () -> purchaseRequisition, this::createPlanning);
        return release;
    }
}
