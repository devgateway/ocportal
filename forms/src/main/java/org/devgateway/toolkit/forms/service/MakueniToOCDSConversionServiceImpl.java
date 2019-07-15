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
import org.devgateway.ocds.persistence.mongo.repository.main.ReleaseRepository;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.FileMetadata;
import org.devgateway.toolkit.persistence.dao.categories.ProcurementMethod;
import org.devgateway.toolkit.persistence.dao.categories.ProcuringEntity;
import org.devgateway.toolkit.persistence.dao.form.AbstractMakueniEntity;
import org.devgateway.toolkit.persistence.dao.form.AwardAcceptance;
import org.devgateway.toolkit.persistence.dao.form.AwardNotification;
import org.devgateway.toolkit.persistence.dao.form.Bid;
import org.devgateway.toolkit.persistence.dao.form.ContractDocument;
import org.devgateway.toolkit.persistence.dao.form.PurchaseItem;
import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisition;
import org.devgateway.toolkit.persistence.dao.form.Statusable;
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
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MakueniToOCDSConversionServiceImpl implements MakueniToOCDSConversionService {

    @Autowired
    private ReleaseRepository releaseRepository;

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

        safeSet(ocdsTender::setStatus, () -> tender, this::createTenderStatus);
        safeSet(ocdsTender::setNumberOfTenderers,
                () -> tender.getPurchaseRequisition().getSingleTenderQuotationEvaluation(),
                this::convertNumberOfTenderers
        );

        //documents
        safeSet(ocdsTender.getDocuments()::add, tender::getFormDoc, this::storeAsDocumentTenderNotice);
        safeSet(ocdsTender.getDocuments()::add, tender::getTenderLink, this::createDocumentFromUrlTenderNotice);
        safeSet(ocdsTender.getDocuments()::add, tender.getPurchaseRequisition()::getFormDoc,
                this::storeAsDocumentApprovedPurchaseRequisition);

        safeSetEach(ocdsTender.getDocuments()::add,
                () -> Optional.ofNullable(tender.getPurchaseRequisition().getSingleTenderQuotationEvaluation())
                        .map(TenderQuotationEvaluation::getFormDocs).orElse(null),
                this::storeAsDocumentEvaluationReports);


        return ocdsTender;
    }


    public Integer convertNumberOfTenderers(TenderQuotationEvaluation tenderQuotationEvaluation) {
        return tenderQuotationEvaluation.getBids().size();
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


    private Document storeAsDocumentAwardNotice(FileMetadata fm) {
        return mongoFileStorageService.storeFileAndReferenceAsDocument(fm, Document.DocumentType.AWARD_NOTICE);
    }

    private Document storeAsDocumentProfessionalOpinion(FileMetadata fm) {
        return mongoFileStorageService.storeFileAndReferenceAsDocument(
                fm,
                Document.DocumentType.X_EVALUATION_PROFESSIONAL_OPINION
        );
    }

    private Document storeAsDocumentAwardAcceptance(FileMetadata fm) {
        return mongoFileStorageService.storeFileAndReferenceAsDocument(
                fm,
                Document.DocumentType.X_AWARD_ACCEPTANCE
        );
    }


    private Document storeAsDocumentEvaluationReports(FileMetadata fm) {
        return mongoFileStorageService.storeFileAndReferenceAsDocument(
                fm, Document.DocumentType.EVALUATION_REPORTS);
    }

    private Document storeAsDocumentApprovedPurchaseRequisition(FileMetadata fm) {
        return mongoFileStorageService.storeFileAndReferenceAsDocument(fm,
                Document.DocumentType.X_APPROVED_PURCHASE_REQUISITION);
    }

    private Document storeAsDocumentContractNotice(ContractDocument contractDocument) {
        return mongoFileStorageService.storeFileAndReferenceAsDocument(
                PersistenceUtil.getNext(contractDocument.getFormDocs()),
                contractDocument.getContractDocumentType().getLabel()
        );
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

    @Override
    public Release createAndPersistRelease(PurchaseRequisition purchaseRequisition) {
        Release release = createRelease(purchaseRequisition);
        Release byOcid = releaseRepository.findByOcid(release.getOcid());
        if (byOcid != null) {
            releaseRepository.delete(byOcid);
        }

        return releaseRepository.save(release);
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
        if (o instanceof Statusable && !((Statusable) o).isExportable()) {
            return;
        }
        if (!ObjectUtils.isEmpty(o)) {
            C converted = converter.apply(o);
            if (!ObjectUtils.isEmpty(converted)) {
                consumer.accept(converted);
            }
            }
    }

    public <S, C> void safeSetEach(Consumer<C> consumer, Supplier<Collection<S>> supplier, Function<S, C> converter) {
        if (supplier == null || consumer == null || converter == null) {
            return;
        }

        Collection<S> o = supplier.get();
        if (o instanceof Statusable && !((Statusable) o).isExportable()) {
            return;
        }
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

    public Set<Organization> createTenderersFromBids(Bids bids) {
        return bids.getDetails().stream().flatMap(b -> b.getTenderers().stream()).collect(Collectors.toSet());
    }


    public Detail createBidsDetail(Bid bid) {
        Detail detail = new Detail();
        safeSet(detail.getTenderers()::add, bid::getSupplier, this::convertSupplier);
        safeSet(detail::setValue, bid::getQuotedAmount, this::convertAmount);
        safeSet(detail::setStatus, () -> bid, this::createBidStatus);
        return detail;
    }

    public String createBidStatus(Bid bid) {
        if (DBConstants.SupplierResponsiveness.PASS.equals(bid.getSupplierResponsiveness())) {
            return "valid";
        }

        return "disqualified";
    }


    public Award createAward(AwardNotification awardNotification) {
        Award ocdsAward = new Award();
        safeSet(ocdsAward::setTitle, awardNotification.getPurchaseRequisition().getSingleTender()::getTenderTitle);
        safeSet(ocdsAward::setId, awardNotification.getPurchaseRequisition().getSingleTender()::getTenderNumber);
        safeSet(ocdsAward::setDate, awardNotification::getAwardDate);
        safeSet(ocdsAward::setValue, awardNotification::getAwardValue, this::convertAmount);
        safeSet(ocdsAward.getSuppliers()::add, awardNotification::getAwardee, this::convertSupplier);
        safeSet(ocdsAward::setContractPeriod, awardNotification::getAcknowledgementDays, this::convertDaysToPeriod);
        safeSet(ocdsAward.getDocuments()::add, awardNotification::getFormDoc, this::storeAsDocumentAwardNotice);
        safeSet(
                ocdsAward.getDocuments()::add,
                awardNotification.getPurchaseRequisition().getSingleProfessionalOpinion()::getFormDoc,
                this::storeAsDocumentProfessionalOpinion
        );

        safeSet(
                ocdsAward.getDocuments()::add,
                () -> Optional.ofNullable(awardNotification.getPurchaseRequisition().getSingleAwardAcceptance())
                        .filter(Statusable::isExportable).map(AwardAcceptance::getFormDoc).orElse(null),
                this::storeAsDocumentAwardAcceptance
        );


        //this will overwrite the award value taken from award notification with a possible different award value
        //from award acceptance (if any)
        safeSet(
                ocdsAward::setValue,
                () -> Optional.ofNullable(awardNotification.getPurchaseRequisition().getSingleAwardAcceptance())
                        .filter(Statusable::isExportable)
                        .map(AwardAcceptance::getAcceptedAwardValue).orElse(null),
                this::convertAmount
        );

        safeSet(ocdsAward::setStatus, () -> awardNotification, this::createAwardStatus);


        return ocdsAward;
    }

    @Override
    public Contract createContract(org.devgateway.toolkit.persistence.dao.form.Contract contract) {
        Contract ocdsContract = new Contract();
        safeSet(ocdsContract::setId, contract::getReferenceNumber);
        safeSet(ocdsContract::setTitle, contract.getPurchaseRequisition().getSingleTender()::getTenderTitle);
        safeSet(ocdsContract::setDateSigned, contract::getContractDate);
        safeSet(ocdsContract::setPeriod, contract::getExpiryDate, this::convertContractEndDateToPeriod);
        safeSet(ocdsContract::setValue, contract::getContractValue, this::convertAmount);
        safeSet(ocdsContract::setDateSigned, contract::getApprovedDate);
        safeSetEach(ocdsContract.getDocuments()::add, contract::getContractDocs, this::storeAsDocumentContractNotice);
        safeSet(ocdsContract::setStatus, contract::getStatus, this::createContractStatus);

        return ocdsContract;
    }

    public Contract.Status createContractStatus(String contractStatus) {
        if (DBConstants.Status.APPROVED.equals(contractStatus)) {
            return Contract.Status.active;
        }
        if (DBConstants.Status.TERMINATED.equals(contractStatus)) {
            return Contract.Status.cancelled;
        }

        return Contract.Status.pending;
    }

    /**
     * OCMAKU-174
     *
     * @param awardNotification
     * @return
     */
    public Award.Status createAwardStatus(AwardNotification awardNotification) {

        //Cancelled: if Terminated at Notification of award or later stage
        if (awardNotification.isTerminated()
                || Optional.ofNullable(awardNotification.getPurchaseRequisition().getSingleAwardAcceptance())
                .map(AwardAcceptance::isTerminated).orElse(false)
                || Optional.ofNullable(awardNotification.getPurchaseRequisition().getSingleContract())
                .map(org.devgateway.toolkit.persistence.dao.form.Contract::isTerminated).orElse(false)
        ) {
            return Award.Status.cancelled;
        }

        //Active: When Acceptance of award has been approved
        if (DBConstants.Status.APPROVED.equals(
                Optional.ofNullable(awardNotification.getPurchaseRequisition().getSingleAwardAcceptance())
                        .map(Statusable::getStatus).orElse(null))) {
            return Award.Status.active;
        }

        return Award.Status.pending;
    }

    public Period convertDaysToPeriod(Integer days) {
        Period period = new Period();
        period.setDurationInDays(days);
        return period;
    }


    public Period convertContractEndDateToPeriod(Date endDate) {
        Period period = new Period();
        period.setEndDate(endDate);
        return period;
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
        safeSet(release::setBids, purchaseRequisition::getSingleTenderQuotationEvaluation, this::createBids);
        safeSet(release::setTender, purchaseRequisition::getSingleTender, this::createTender);

        safeSet(Optional.ofNullable(release.getTender()).map(Tender::getTenderers).orElse(new HashSet<>())::addAll,
                release::getBids, this::createTenderersFromBids
        );

        safeSet(release.getAwards()::add, purchaseRequisition::getSingleAwardNotification, this::createAward);
        safeSet(release.getContracts()::add, purchaseRequisition::getSingleContract, this::createContract);
        return release;
    }
}
