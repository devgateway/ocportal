package org.devgateway.toolkit.persistence.dao.form;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.categories.ChargeAccount;
import org.devgateway.toolkit.persistence.dao.categories.Staff;
import org.devgateway.toolkit.persistence.excel.annotation.ExcelExport;
import org.devgateway.toolkit.persistence.spring.PersistenceUtil;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.envers.Audited;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @author idobre
 * @since 2019-04-17
 */
@Entity
@Audited
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(indexes = {@Index(columnList = "project_id"),
        @Index(columnList = "purchaseRequestNumber"),
        @Index(columnList = "title")})
public class PurchaseRequisition extends AbstractMakueniEntity implements ProjectAttachable, ProcurementPlanAttachable,
        TitleAutogeneratable {
    @ManyToOne(fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Project project;

    @ExcelExport(useTranslation = true)
    @Column(length = DBConstants.STD_DEFAULT_TEXT_LENGTH)
    private String purchaseRequestNumber;

    @ExcelExport(useTranslation = true)
    @Column(length = DBConstants.STD_DEFAULT_TEXT_LENGTH)
    private String title;

    @ExcelExport(justExport = true, useTranslation = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToOne
    private Staff requestedBy;

    @ExcelExport(justExport = true, useTranslation = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToOne
    private ChargeAccount chargeAccount;

    @ExcelExport(useTranslation = true)
    private Date requestApprovalDate;

    @ExcelExport(name = "Purchase Items", separateSheet = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "parent_id")
    @OrderColumn(name = "index")
    private List<PurchaseItem> purchaseItems = new ArrayList<>();

    @ExcelExport(separateSheet = true, name = "Tender")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "purchaseRequisition")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Set<Tender> tender = new HashSet<>();

    @ExcelExport(separateSheet = true, name = "Tender Quotation Evaluation")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "purchaseRequisition")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Set<TenderQuotationEvaluation> tenderQuotationEvaluation = new HashSet<>();

    @ExcelExport(separateSheet = true, name = "Professional Opinion")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "purchaseRequisition")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Set<ProfessionalOpinion> professionalOpinion = new HashSet<>();

    @ExcelExport(separateSheet = true, name = "Award Notification")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "purchaseRequisition")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Set<AwardNotification> awardNotification = new HashSet<>();

    @ExcelExport(separateSheet = true, name = "Award Acceptance")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "purchaseRequisition")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Set<AwardAcceptance> awardAcceptance = new HashSet<>();

    @ExcelExport(separateSheet = true, name = "Contract")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "purchaseRequisition")
    @LazyToOne(value = LazyToOneOption.NO_PROXY)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Set<Contract> contract = new HashSet<>();

    private boolean checkTerminated(Statusable... statusables) {
        for (Statusable statusable : statusables) {
            if (statusable != null && statusable.isTerminated()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Calculates if this {@link PurchaseRequisition} is terminated. This involves going through all stages and
     * checking if any of them is terminated
     * @return
     */
    @Override
    public boolean isTerminated() {
        return checkTerminated(
                PersistenceUtil.getNext(tender), PersistenceUtil.getNext(tenderQuotationEvaluation),
                PersistenceUtil.getNext(professionalOpinion), PersistenceUtil.getNext(awardNotification),
                PersistenceUtil.getNext(awardAcceptance), PersistenceUtil.getNext(contract)
        );
    }

    public Tender getSingleTender() {
        return PersistenceUtil.getNext(tender);
    }

    public TenderQuotationEvaluation getSingleTenderQuotationEvaluation() {
        return PersistenceUtil.getNext(tenderQuotationEvaluation);
    }

    public ProfessionalOpinion getSingleProfessionalOpinion() {
        return PersistenceUtil.getNext(professionalOpinion);
    }

    public AwardNotification getSingleAwardNotification() {
        return PersistenceUtil.getNext(awardNotification);
    }

    public AwardAcceptance getSingleAwardAcceptance() {
        return PersistenceUtil.getNext(awardAcceptance);
    }

    public Contract getSingleContract() {
        return PersistenceUtil.getNext(contract);
    }

    @Override
    public Project getProject() {
        return project;
    }

    public void setProject(final Project project) {
        this.project = project;
    }

    public String getPurchaseRequestNumber() {
        return purchaseRequestNumber;
    }

    public void setPurchaseRequestNumber(final String purchaseRequestNumber) {
        this.purchaseRequestNumber = purchaseRequestNumber;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public Consumer<String> titleSetter() {
        return this::setTitle;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public Staff getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(final Staff requestedBy) {
        this.requestedBy = requestedBy;
    }

    public ChargeAccount getChargeAccount() {
        return chargeAccount;
    }

    public void setChargeAccount(final ChargeAccount chargeAccount) {
        this.chargeAccount = chargeAccount;
    }

    public Date getRequestApprovalDate() {
        return requestApprovalDate;
    }

    public void setRequestApprovalDate(final Date requestApprovalDate) {
        this.requestApprovalDate = requestApprovalDate;
    }

    public List<PurchaseItem> getPurchaseItems() {
        return purchaseItems;
    }

    public void setPurchaseItems(final List<PurchaseItem> purchaseItems) {
        this.purchaseItems = purchaseItems;
    }

    @Override
    public void setLabel(final String label) {

    }

    @Override
    public String getLabel() {
        return title;
    }

    @Override
    public String toString() {
        return getLabel();
    }

    public BigDecimal getAmount() {
        BigDecimal amount = BigDecimal.ZERO;
        for (PurchaseItem item : purchaseItems) {
            if (item.getAmount() != null && item.getQuantity() != null) {
                amount.add(item.getAmount().multiply(new BigDecimal(item.getQuantity())));
            }
        }

        return amount;
    }


    public Set<Tender> getTender() {
        return tender;
    }

    public void setTender(Set<Tender> tender) {
        this.tender = tender;
    }

    public void addTender(final Tender item) {
        tender.add(item);
        item.setPurchaseRequisition(this);
    }

    public void removeTender(final Tender item) {
        tender.remove(item);
        item.setPurchaseRequisition(null);
    }

    public Set<TenderQuotationEvaluation> getTenderQuotationEvaluation() {
        return tenderQuotationEvaluation;
    }

    public void setTenderQuotationEvaluation(Set<TenderQuotationEvaluation> tenderQuotationEvaluation) {
        this.tenderQuotationEvaluation = tenderQuotationEvaluation;
    }

    public void addTenderQuotationEvaluation(final TenderQuotationEvaluation item) {
        tenderQuotationEvaluation.add(item);
        item.setPurchaseRequisition(this);
    }

    public void removeTenderQuotationEvaluation(final TenderQuotationEvaluation item) {
        tenderQuotationEvaluation.remove(item);
        item.setPurchaseRequisition(null);
    }

    public Set<ProfessionalOpinion> getProfessionalOpinion() {
        return professionalOpinion;
    }

    public void setProfessionalOpinion(Set<ProfessionalOpinion> professionalOpinion) {
        this.professionalOpinion = professionalOpinion;
    }

    public void addProfessionalOpinion(final ProfessionalOpinion item) {
        professionalOpinion.add(item);
        item.setPurchaseRequisition(this);
    }

    public void removeProfessionalOpinion(final ProfessionalOpinion item) {
        professionalOpinion.remove(item);
        item.setPurchaseRequisition(null);
    }

    public Set<AwardNotification> getAwardNotification() {
        return awardNotification;
    }

    public void setAwardNotification(Set<AwardNotification> awardNotification) {
        this.awardNotification = awardNotification;
    }

    public void addAwardNotification(final AwardNotification item) {
        awardNotification.add(item);
        item.setPurchaseRequisition(this);
    }

    public void removeAwardNotification(final AwardNotification item) {
        awardNotification.remove(item);
        item.setPurchaseRequisition(null);
    }

    public Set<AwardAcceptance> getAwardAcceptance() {
        return awardAcceptance;
    }

    public void setAwardAcceptance(Set<AwardAcceptance> awardAcceptance) {
        this.awardAcceptance = awardAcceptance;
    }

    public void addAwardAcceptance(final AwardAcceptance item) {
        awardAcceptance.add(item);
        item.setPurchaseRequisition(this);
    }

    public void removeAwardAcceptance(final AwardAcceptance item) {
        awardAcceptance.remove(item);
        item.setPurchaseRequisition(null);
    }

    public Set<Contract> getContract() {
        return contract;
    }

    public void setContract(Set<Contract> contract) {
        this.contract = contract;
    }

    public void addContract(final Contract item) {
        contract.add(item);
        item.setPurchaseRequisition(this);
    }

    public void removeContract(final Contract item) {
        contract.remove(item);
        item.setPurchaseRequisition(null);
    }

    @Override
    public ProcurementPlan getProcurementPlan() {
        if (project != null) {
            return project.getProcurementPlan();
        }
        return null;
    }

    @Override
    @Transactional
    public Collection<AbstractMakueniEntity> getDirectChildrenEntities() {
        return Collections.singletonList(PersistenceUtil.getNext(tender));
    }

}
