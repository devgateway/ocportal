package org.devgateway.toolkit.forms.service;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.Validate;
import org.devgateway.ocds.persistence.mongo.Address;
import org.devgateway.ocds.persistence.mongo.Amount;
import org.devgateway.ocds.persistence.mongo.Award;
import org.devgateway.ocds.persistence.mongo.Bids;
import org.devgateway.ocds.persistence.mongo.Budget;
import org.devgateway.ocds.persistence.mongo.Classification;
import org.devgateway.ocds.persistence.mongo.ContactPoint;
import org.devgateway.ocds.persistence.mongo.Contract;
import org.devgateway.ocds.persistence.mongo.Detail;
import org.devgateway.ocds.persistence.mongo.Document;
import org.devgateway.ocds.persistence.mongo.Identifier;
import org.devgateway.ocds.persistence.mongo.Item;
import org.devgateway.ocds.persistence.mongo.MakueniPlanning;
import org.devgateway.ocds.persistence.mongo.Milestone;
import org.devgateway.ocds.persistence.mongo.Organization;
import org.devgateway.ocds.persistence.mongo.Period;
import org.devgateway.ocds.persistence.mongo.Planning;
import org.devgateway.ocds.persistence.mongo.Release;
import org.devgateway.ocds.persistence.mongo.Tender;
import org.devgateway.ocds.persistence.mongo.Unit;
import org.devgateway.toolkit.persistence.dao.FileMetadata;
import org.devgateway.toolkit.persistence.dao.categories.ProcurementMethod;
import org.devgateway.toolkit.persistence.dao.categories.ProcuringEntity;
import org.devgateway.toolkit.persistence.dao.form.AbstractMakueniEntity;
import org.devgateway.toolkit.persistence.dao.form.AwardAcceptance;
import org.devgateway.toolkit.persistence.dao.form.AwardNotification;
import org.devgateway.toolkit.persistence.dao.form.Bid;
import org.devgateway.toolkit.persistence.dao.form.ProfessionalOpinion;
import org.devgateway.toolkit.persistence.dao.form.PurchaseItem;
import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisition;
import org.devgateway.toolkit.persistence.dao.form.TenderItem;
import org.devgateway.toolkit.persistence.dao.form.TenderQuotationEvaluation;
import org.devgateway.toolkit.persistence.spring.PersistenceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.net.URI;
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

    private ImmutableMap<String, Tender.ProcurementMethod> procurementMethodMap;

    @Autowired
    private MongoFileStorageService mongoFileStorageService;

    @Override
    public Tender createTender(org.devgateway.toolkit.persistence.dao.form.Tender tender) {
        Tender ocdsTender = new Tender();
        safeSet(ocdsTender::setId, tender::getId, this::longIdToString);
        safeSet(ocdsTender::setTitle, tender::getTitle);
        safeSet(ocdsTender::setTenderPeriod, () -> tender, this::createTenderPeriod);
        safeSet(ocdsTender::setProcurementMethod, tender::getProcurementMethod, this::createProcurementMethod);
        safeSetEach(ocdsTender.getItems()::add, tender::getTenderItems, this::createTenderItem);
        safeSet(ocdsTender::setDescription, tender::getObjective);
        safeSet(ocdsTender::setProcuringEntity, tender::getIssuedBy, this::createProcuringEntity);
        safeSet(ocdsTender::setValue, tender::getTenderValue, this::convertAmount);
        safeSet(ocdsTender.getDocuments()::add, tender::getFormDoc, this::storeAsDocumentTenderNotice);
        safeSet(ocdsTender.getDocuments()::add, tender::getTenderLink, this::createDocumentFromUrlTenderNotice);
        safeSet(ocdsTender::setStatus, () -> tender, this::createTenderStatus);
        safeSet(
                ocdsTender::setNumberOfTenderers,
                () -> PersistenceUtil.getNext(tender.getPurchaseRequisition().getTenderQuotationEvaluation()),
                this::convertNumberOfTenderers
        );
        return ocdsTender;
    }


    public Integer convertNumberOfTenderers(TenderQuotationEvaluation tenderQuotationEvaluations) {
        return tenderQuotationEvaluations.getNumberOfBids();
    }

    public Tender.Status createTenderStatus(org.devgateway.toolkit.persistence.dao.form.Tender tender) {
        if (tender.isTerminated()) {
            return Tender.Status.cancelled;
        }
        //TODO: finish this !!
        return Tender.Status.active;
    }

    public Organization createProcuringEntity(ProcuringEntity procuringEntity) {
        Organization ocdsProcuringEntity = new Organization();
        safeSet(ocdsProcuringEntity::setId, procuringEntity::getId, this::longIdToString);
        safeSet(ocdsProcuringEntity::setAddress, () -> procuringEntity, this::createProcuringEntityAddress);
        safeSet(ocdsProcuringEntity::setName, procuringEntity::getLabel);
        safeSet(ocdsProcuringEntity::setContactPoint, () -> procuringEntity, this::createProcuringEntityContactPoint);
        return ocdsProcuringEntity;
    }

    public ContactPoint createProcuringEntityContactPoint(ProcuringEntity procuringEntity) {
        ContactPoint ocdsContactPoint = new ContactPoint();
        safeSet(ocdsContactPoint::setEmail, procuringEntity::getEmailAddress);
        return ocdsContactPoint;
    }

    public Address createProcuringEntityAddress(ProcuringEntity procuringEntity) {
        Address ocdsAddress = new Address();
        safeSet(ocdsAddress::setCountryName, this::getCountry);
        safeSet(ocdsAddress::setStreetAddress, procuringEntity::getAddress);
        return ocdsAddress;
    }

    public String getCountry() {
        return "Kenya";
    }


    @Override
    public Item createTenderItem(TenderItem tenderItem) {
        Item ocdsItem = new Item();
        safeSet(ocdsItem::setId, tenderItem::getId, this::longIdToString);
        safeSet(ocdsItem::setUnit, () -> tenderItem, this::createTenderItemUnit);
        safeSet(ocdsItem::setQuantity, tenderItem::getQuantity, Integer::doubleValue);
        safeSet(ocdsItem::setClassification, () -> tenderItem, this::createTenderItemClassification);
        return ocdsItem;
    }


    @Override
    public Unit createTenderItemUnit(TenderItem tenderItem) {
        Unit unit = new Unit();
        safeSet(unit::setScheme, () -> "scheme");
        safeSet(unit::setName, tenderItem.getPurchaseItem()::getUnit);
        safeSet(unit::setId, tenderItem::getId, this::longIdToString);
        safeSet(unit::setValue, tenderItem::getUnitPrice, this::convertAmount);
        return unit;
    }


    @Override
    public Classification createTenderItemClassification(TenderItem tenderItem) {
        Classification classification = new Classification();
        safeSet(classification::setId, tenderItem.getPurchaseItem().getPlanItem().getItem()::getCode);
        safeSet(classification::setDescription, tenderItem.getPurchaseItem().getPlanItem()::getDescription);
        return classification;
    }

    @Override
    public Tender.ProcurementMethod createProcurementMethod(ProcurementMethod procurementMethod) {
        return procurementMethodMap.get(procurementMethod.getLabel());
    }

    @PostConstruct
    public void init() {
        procurementMethodMap = ImmutableMap.<String, Tender.ProcurementMethod>builder()
                .put("Direct", Tender.ProcurementMethod.direct)
                .put("Open tender national", Tender.ProcurementMethod.open)
                .put("RF proposal", Tender.ProcurementMethod.limited)
                .put("RFQ", Tender.ProcurementMethod.selective)
                .put("Restricted tender", Tender.ProcurementMethod.limited)
                .put("Special permitted", Tender.ProcurementMethod.limited)
                .put("Low value procurement", Tender.ProcurementMethod.direct)
                .put("Open tender International", Tender.ProcurementMethod.open).build();
    }

    @Override
    public Period createTenderPeriod(org.devgateway.toolkit.persistence.dao.form.Tender tender) {
        Period period = new Period();
        safeSet(period::setStartDate, tender::getInvitationDate);
        safeSet(period::setEndDate, tender::getClosingDate);
        return period;
    }


    @Override
    public Budget createPlanningBudget(PurchaseRequisition purchaseRequisition) {
        Budget budget = new Budget();

        safeSet(budget::setProjectID, purchaseRequisition.getProject()::getProjectTitle);
        safeSet(budget::setAmount, purchaseRequisition.getProject()::getAmountBudgeted, this::convertAmount);

        return budget;
    }


    private Document storeAsDocumentProcurementPlan(FileMetadata fm) {
        return mongoFileStorageService.storeFileAndReferenceAsDocument(fm, Document.DocumentType.PROCUREMENT_PLAN);
    }

    private Document storeAsDocumentProjectPlan(AbstractMakueniEntity entity) {
        return mongoFileStorageService.storeFileAndReferenceAsDocument(
                entity.getFormDoc(),
                Document.DocumentType.PROJECT_PLAN
        );
    }

    private Document storeAsDocumentTenderNotice(FileMetadata fm) {
        return mongoFileStorageService.storeFileAndReferenceAsDocument(fm, Document.DocumentType.TENDER_NOTICE);
    }

    private Document createDocumentFromUrlTenderNotice(String url) {
        return createDocumentFromUrl(url, Document.DocumentType.TENDER_NOTICE);
    }

    private Document createDocumentFromUrl(String url, Document.DocumentType documentType) {
        Document document = new Document();
        safeSet(document::setUrl, () -> url, URI::create);
        safeSet(document::setDocumentType, documentType::toString);
        return document;
    }

    @Override
    public MakueniPlanning createPlanning(PurchaseRequisition purchaseRequisition) {
        MakueniPlanning planning = new MakueniPlanning();

        safeSet(planning::setBudget, () -> purchaseRequisition, this::createPlanningBudget);

        //TODO: set planning extension items
        safeSetEach(planning.getItems()::add, purchaseRequisition::getPurchaseItems, this::createPlanningItem);

        safeSet(planning.getDocuments()::add, purchaseRequisition.getProcurementPlan()::getFormDoc,
                this::storeAsDocumentProcurementPlan
        );

        //TODO: also set document title to something else than uploaded document title?
        safeSet(planning.getDocuments()::add, purchaseRequisition.getProject()::getCabinetPaper,
                this::storeAsDocumentProjectPlan
        );

        safeSet(planning.getMilestones()::add, () -> purchaseRequisition, this::createPlanningMilestone);

        return planning;
    }


    public Organization convertSupplier(org.devgateway.toolkit.persistence.dao.categories.Supplier supplier) {
        Organization ocdsOrg = new Organization();
        safeSet(ocdsOrg::setName, supplier::getLabel);
        safeSet(ocdsOrg::setIdentifier, () -> supplier, this::convertSupplierId);
        safeSet(ocdsOrg::setAddress, () -> supplier, this::createSupplierAddress);
        safeSet(ocdsOrg.getRoles()::add, () -> Organization.OrganizationType.supplier,
                Organization.OrganizationType::toValue
        );
        return ocdsOrg;
    }

    public Address createSupplierAddress(org.devgateway.toolkit.persistence.dao.categories.Supplier supplier) {
        Address ocdsAddress = new Address();
        safeSet(ocdsAddress::setCountryName, this::getCountry);
        safeSet(ocdsAddress::setStreetAddress, supplier::getAddress);
        return ocdsAddress;
    }


    public Identifier convertSupplierId(org.devgateway.toolkit.persistence.dao.categories.Supplier supplier) {
        Identifier identifier = new Identifier();
        safeSet(identifier::setId, supplier::getCode);
        safeSet(identifier::setLegalName, supplier::getLabel);
        return identifier;
    }


    @Override
    public Milestone createPlanningMilestone(PurchaseRequisition purchaseRequisition) {
        Milestone milestone = new Milestone();
        safeSet(milestone::setType, () -> Milestone.MilestoneType.PRE_PROCUREMENT, Milestone.MilestoneType::toValue);
        safeSet(milestone::setCode, () -> "approvedDate");
        safeSet(milestone::setDateMet, purchaseRequisition::getApprovedDate);
        safeSet(milestone::setStatus, () -> purchaseRequisition, this::createPlanningMilestoneStatus);
        return milestone;
    }


    public Milestone.Status createPlanningMilestoneStatus(PurchaseRequisition purchaseRequisition) {
        //TODO: implement more statuses
        return Milestone.Status.SCHEDULED;
    }


    public <C, S, R extends Supplier<S>> Supplier<S> getSupplier(Supplier<C> parentSupplier,
                                                                 Function<C, R> childSupplier) {
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
        safeSet(unit::setValue, purchaseItem::getAmount, this::convertAmount);
        return unit;
    }

    @Override
    public Amount convertAmount(BigDecimal sourceAmount) {
        Amount amount = new Amount();
        safeSet(amount::setAmount, () -> sourceAmount);
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
        Bids bids = new Bids();
        safeSetEach(bids.getDetails()::add, quotationEvaluation::getBids, this::createBidsDetail);


        return bids;

    }


    public Detail createBidsDetail(Bid bid) {
        Detail detail = new Detail();
        safeSet(detail.getTenderers()::add, bid::getSupplier, this::convertSupplier);
        safeSet(detail::setValue, bid::getQuotedAmount, this::convertAmount);
        return detail;
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
        safeSet(release::setTender, () -> PersistenceUtil.getNext(purchaseRequisition.getTender()), this::createTender);
        return release;
    }
}
