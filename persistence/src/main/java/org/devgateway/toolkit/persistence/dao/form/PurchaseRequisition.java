package org.devgateway.toolkit.persistence.dao.form;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.categories.ChargeAccount;
import org.devgateway.toolkit.persistence.dao.categories.Staff;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
public class PurchaseRequisition extends AbstractMakueniEntity implements ProjectAttachable {

    @ManyToOne(fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Project project;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "purchaseRequisition")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private TenderQuotationEvaluation tenderQuotationEvaluation;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "purchaseRequisition")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Tender tender;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "purchaseRequisition")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Contract contract;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "purchaseRequisition")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private AwardNotification awardNotification;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "purchaseRequisition")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private AwardAcceptance awardAcceptance;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "purchaseRequisition")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private ProfessionalOpinion professionalOpinion;

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

    public TenderQuotationEvaluation getTenderQuotationEvaluation() {
        return tenderQuotationEvaluation;
    }

    public void setTenderQuotationEvaluation(TenderQuotationEvaluation tenderQuotationEvaluation) {
        this.tenderQuotationEvaluation = tenderQuotationEvaluation;
    }

    public Tender getTender() {
        return tender;
    }

    public void setTender(Tender tender) {
        this.tender = tender;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public AwardNotification getAwardNotification() {
        return awardNotification;
    }

    public void setAwardNotification(AwardNotification awardNotification) {
        this.awardNotification = awardNotification;
    }

    public AwardAcceptance getAwardAcceptance() {
        return awardAcceptance;
    }

    public void setAwardAcceptance(AwardAcceptance awardAcceptance) {
        this.awardAcceptance = awardAcceptance;
    }

    public ProfessionalOpinion getProfessionalOpinion() {
        return professionalOpinion;
    }

    public void setProfessionalOpinion(ProfessionalOpinion professionalOpinion) {
        this.professionalOpinion = professionalOpinion;
    }
}
