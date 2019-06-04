package org.devgateway.toolkit.persistence.dao.form;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.categories.ChargeAccount;
import org.devgateway.toolkit.persistence.dao.categories.Staff;
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
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

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
public class PurchaseRequisition extends AbstractMakueniEntity implements ProjectAttachable, ProcurementPlanAttachable {

    @ManyToOne(fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @NotNull
    private Project project;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "purchaseRequisition")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Set<Tender> tender;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "purchaseRequisition")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Set<TenderQuotationEvaluation> tenderQuotationEvaluation;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "purchaseRequisition")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Set<ProfessionalOpinion> professionalOpinion;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "purchaseRequisition")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Set<AwardNotification> awardNotification;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "purchaseRequisition")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Set<AwardAcceptance> awardAcceptance;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "purchaseRequisition")
    @LazyToOne(value = LazyToOneOption.NO_PROXY)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Set<Contract> contract;

    @Column(length = DBConstants.STD_DEFAULT_TEXT_LENGTH)
    private String purchaseRequestNumber;

    @Column(length = DBConstants.STD_DEFAULT_TEXT_LENGTH)
    private String title;

    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToOne
    private Staff requestedBy;

    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToOne
    private ChargeAccount chargeAccount;

    private Date requestApprovalDate;

    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "parent_id")
    @OrderColumn(name = "index")
    private List<PurchaseItem> purchaseItems = new ArrayList<>();

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

    public String getTitle() {
        return title;
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

    public Double getAmount() {
        Double amount = 0d;
        for (PurchaseItem item : purchaseItems) {
            if (item.getAmount() != null && item.getQuantity() != null) {
                amount += (item.getAmount() * item.getQuantity());
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

    public Set<TenderQuotationEvaluation> getTenderQuotationEvaluation() {
        return tenderQuotationEvaluation;
    }

    public void setTenderQuotationEvaluation(Set<TenderQuotationEvaluation> tenderQuotationEvaluation) {
        this.tenderQuotationEvaluation = tenderQuotationEvaluation;
    }

    public Set<ProfessionalOpinion> getProfessionalOpinion() {
        return professionalOpinion;
    }

    public void setProfessionalOpinion(Set<ProfessionalOpinion> professionalOpinion) {
        this.professionalOpinion = professionalOpinion;
    }

    public Set<AwardNotification> getAwardNotification() {
        return awardNotification;
    }

    public void setAwardNotification(Set<AwardNotification> awardNotification) {
        this.awardNotification = awardNotification;
    }

    public Set<AwardAcceptance> getAwardAcceptance() {
        return awardAcceptance;
    }

    public void setAwardAcceptance(Set<AwardAcceptance> awardAcceptance) {
        this.awardAcceptance = awardAcceptance;
    }

    public Set<Contract> getContract() {
        return contract;
    }

    public void setContract(Set<Contract> contract) {
        this.contract = contract;
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
