package org.devgateway.toolkit.persistence.dao.form;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.devgateway.toolkit.persistence.dao.AbstractAuditableEntity;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.Labelable;
import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.excel.annotation.ExcelExport;
import org.devgateway.toolkit.persistence.spring.PersistenceUtil;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.AccessType;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author idobre
 * @since 2019-04-17
 */
@Entity
@Audited
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(indexes = {@Index(columnList = "project_id")})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TenderProcess extends AbstractAuditableEntity implements Labelable, ProjectAttachable,
        ProcurementPlanAttachable, DepartmentAttachable, Terminatable {
    @ManyToOne(fetch = FetchType.EAGER)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "procurement_plan_id")
    @JsonIgnore
    @org.springframework.data.annotation.Transient
    private ProcurementPlan procurementPlan;


    @ExcelExport(separateSheet = true, name = "Tender")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tenderProcess")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Set<Tender> tender = new HashSet<>();

    @ExcelExport(separateSheet = true, name = "Tender Quotation Evaluation")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tenderProcess")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Set<TenderQuotationEvaluation> tenderQuotationEvaluation = new HashSet<>();

    @ExcelExport(separateSheet = true, name = "Purchase Requisition")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tenderProcess")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Set<PurchaseRequisitionGroup> purchaseRequisition = new HashSet<>();

    @ExcelExport(separateSheet = true, name = "Professional Opinion")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tenderProcess")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Set<ProfessionalOpinion> professionalOpinion = new HashSet<>();

    @ExcelExport(separateSheet = true, name = "Award Notification")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tenderProcess")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Set<AwardNotification> awardNotification = new HashSet<>();

    @ExcelExport(separateSheet = true, name = "Award Acceptance")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tenderProcess")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Set<AwardAcceptance> awardAcceptance = new HashSet<>();

    @ExcelExport(separateSheet = true, name = "Contract")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tenderProcess")
    @LazyToOne(value = LazyToOneOption.NO_PROXY)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Set<Contract> contract = new HashSet<>();

    @ExcelExport(separateSheet = true, name = "Administrator Reports")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tenderProcess")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Set<AdministratorReport> administratorReports = new HashSet<>();

    @ExcelExport(separateSheet = true, name = "PMC Reports")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tenderProcess")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Set<PMCReport> pmcReports = new HashSet<>();

    @ExcelExport(separateSheet = true, name = "ME Reports")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tenderProcess")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Set<MEReport> meReports = new HashSet<>();

    @ExcelExport(separateSheet = true, name = "Inspection Reports")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tenderProcess")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Set<InspectionReport> inspectionReports = new HashSet<>();

    @ExcelExport(separateSheet = true, name = "Payment Vouchers")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tenderProcess")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Set<PaymentVoucher> paymentVouchers = new HashSet<>();

    @Override
    @JsonIgnore
    @org.springframework.data.annotation.Transient
    public AbstractAuditableEntity getParent() {
        return null;
    }

    /**
     * Calculates if this {@link TenderProcess} is terminated. This involves going through all stages and
     * checking if any of them is terminated
     *
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public boolean isTerminated() {
        ArrayList<Statusable> entityTree = new ArrayList<>();
        entityTree.add(PersistenceUtil.getNext(purchaseRequisition));
        entityTree.add(PersistenceUtil.getNext(tenderQuotationEvaluation));
        entityTree.add(PersistenceUtil.getNext(professionalOpinion));
        entityTree.add(PersistenceUtil.getNext(awardNotification));
        entityTree.add(PersistenceUtil.getNext(awardAcceptance));
        entityTree.add(PersistenceUtil.getNext(contract));
        entityTree.addAll(administratorReports);
        entityTree.addAll(inspectionReports);
        entityTree.addAll(pmcReports);
        entityTree.addAll(meReports);
        entityTree.addAll(paymentVouchers);
        return PersistenceUtil.checkTerminated(entityTree.toArray(new Statusable[]{}));
    }

    @Transactional(readOnly = true)
    public boolean hasNonDraftImplForms() {
        return hasNonDraftImplForms(administratorReports) || hasNonDraftImplForms(inspectionReports)
                || hasNonDraftImplForms(pmcReports) || hasNonDraftImplForms(meReports)
                || hasNonDraftImplForms(paymentVouchers);
    }

    @Transactional(readOnly = true)
    protected boolean hasNonDraftImplForms(Set<? extends AbstractImplTenderProcessMakueniEntity> s) {
        return s.stream().anyMatch(f -> !DBConstants.Status.DRAFT.equals(f.getStatus()));
    }

    @JsonProperty("tender")
    public Tender getSingleTender() {
        return PersistenceUtil.getNext(tender);
    }

    @JsonProperty("tenderQuotationEvaluation")
    public TenderQuotationEvaluation getSingleTenderQuotationEvaluation() {
        return PersistenceUtil.getNext(tenderQuotationEvaluation);
    }

    @JsonProperty("purchaseRequisition")
    public PurchaseRequisitionGroup getSinglePurchaseRequisition() {
        return PersistenceUtil.getNext(purchaseRequisition);
    }

    @JsonProperty("professionalOpinion")
    public ProfessionalOpinion getSingleProfessionalOpinion() {
        return PersistenceUtil.getNext(professionalOpinion);
    }

    @JsonProperty("awardNotification")
    public AwardNotification getSingleAwardNotification() {
        return PersistenceUtil.getNext(awardNotification);
    }

    @JsonProperty("awardAcceptance")
    public AwardAcceptance getSingleAwardAcceptance() {
        return PersistenceUtil.getNext(awardAcceptance);
    }

    @JsonProperty("contract")
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

    public Set<PurchaseRequisitionGroup> getPurchaseRequisition() {
        return purchaseRequisition;
    }

    public void setPurchaseRequisition(Set<PurchaseRequisitionGroup> purchaseRequisition) {
        this.purchaseRequisition = purchaseRequisition;
    }

    public Set<Tender> getTender() {
        return tender;
    }

    public void setTender(Set<Tender> tender) {
        this.tender = tender;
    }

    public void addTender(final Tender item) {
        tender.add(item);
        item.setTenderProcess(this);
    }

    public void addPurchaseRequisition(final PurchaseRequisitionGroup item) {
        purchaseRequisition.add(item);
        item.setTenderProcess(this);
    }


    public void removeTender(final Tender item) {
        tender.remove(item);
        item.setTenderProcess(null);
    }

    public void removePurchaseRequisition(final PurchaseRequisitionGroup item) {
        purchaseRequisition.remove(item);
        item.setTenderProcess(null);
    }


    public void removeAdministratorReport(final AdministratorReport item) {
        administratorReports.remove(item);
        item.setTenderProcess(null);
    }

    public void removeInspectonReport(final InspectionReport item) {
        inspectionReports.remove(item);
        item.setTenderProcess(null);
    }

    public void removePMCReport(final PMCReport item) {
        pmcReports.remove(item);
        item.setTenderProcess(null);
    }

    public void removeMEReport(final MEReport item) {
        meReports.remove(item);
        item.setTenderProcess(null);
    }

    public void removePaymentVoucher(final PaymentVoucher item) {
        paymentVouchers.remove(item);
        item.setTenderProcess(null);
    }

    public Set<TenderQuotationEvaluation> getTenderQuotationEvaluation() {
        return tenderQuotationEvaluation;
    }

    public void setTenderQuotationEvaluation(Set<TenderQuotationEvaluation> tenderQuotationEvaluation) {
        this.tenderQuotationEvaluation = tenderQuotationEvaluation;
    }

    public void addTenderQuotationEvaluation(final TenderQuotationEvaluation item) {
        tenderQuotationEvaluation.add(item);
        item.setTenderProcess(this);
    }

    public void removeTenderQuotationEvaluation(final TenderQuotationEvaluation item) {
        tenderQuotationEvaluation.remove(item);
        item.setTenderProcess(null);
    }

    public Set<ProfessionalOpinion> getProfessionalOpinion() {
        return professionalOpinion;
    }

    public void setProfessionalOpinion(Set<ProfessionalOpinion> professionalOpinion) {
        this.professionalOpinion = professionalOpinion;
    }

    public void addProfessionalOpinion(final ProfessionalOpinion item) {
        professionalOpinion.add(item);
        item.setTenderProcess(this);
    }

    public void removeProfessionalOpinion(final ProfessionalOpinion item) {
        professionalOpinion.remove(item);
        item.setTenderProcess(null);
    }

    public Set<AwardNotification> getAwardNotification() {
        return awardNotification;
    }

    public void setAwardNotification(Set<AwardNotification> awardNotification) {
        this.awardNotification = awardNotification;
    }

    public void addAwardNotification(final AwardNotification item) {
        awardNotification.add(item);
        item.setTenderProcess(this);
    }

    public void removeAwardNotification(final AwardNotification item) {
        awardNotification.remove(item);
        item.setTenderProcess(null);
    }

    public Set<AwardAcceptance> getAwardAcceptance() {
        return awardAcceptance;
    }

    public void setAwardAcceptance(Set<AwardAcceptance> awardAcceptance) {
        this.awardAcceptance = awardAcceptance;
    }

    public void addAwardAcceptance(final AwardAcceptance item) {
        awardAcceptance.add(item);
        item.setTenderProcess(this);
    }

    public void addAdministratorReport(final AdministratorReport item) {
        administratorReports.add(item);
        item.setTenderProcess(this);
    }

    public void addPMCReport(final PMCReport item) {
        pmcReports.add(item);
        item.setTenderProcess(this);
    }

    public void addMEReport(final MEReport item) {
        meReports.add(item);
        item.setTenderProcess(this);
    }

    public void addPaymentVoucher(final PaymentVoucher item) {
        paymentVouchers.add(item);
        item.setTenderProcess(this);
    }

    public void addInspectionReport(final InspectionReport item) {
        inspectionReports.add(item);
        item.setTenderProcess(this);
    }

    public void removeAwardAcceptance(final AwardAcceptance item) {
        awardAcceptance.remove(item);
        item.setTenderProcess(null);
    }

    public Set<Contract> getContract() {
        return contract;
    }

    public void setContract(Set<Contract> contract) {
        this.contract = contract;
    }

    public void addContract(final Contract item) {
        contract.add(item);
        item.setTenderProcess(this);
    }

    public void removeContract(final Contract item) {
        contract.remove(item);
        item.setTenderProcess(null);
    }

    @Override
    @JsonIgnore
    @org.springframework.data.annotation.Transient
    public ProcurementPlan getProcurementPlan() {
        return procurementPlan;
    }

    @Override
    @Transactional
    @JsonIgnore
    @org.springframework.data.annotation.Transient
    public Department getDepartment() {
        return getProcurementPlan().getDepartment();
    }

    public Set<AdministratorReport> getAdministratorReports() {
        return administratorReports;
    }

    public void setAdministratorReports(Set<AdministratorReport> administratorReports) {
        this.administratorReports = administratorReports;
    }

    public Set<PMCReport> getPmcReports() {
        return pmcReports;
    }

    public void setPmcReports(Set<PMCReport> pmcReports) {
        this.pmcReports = pmcReports;
    }

    public Set<MEReport> getMeReports() {
        return meReports;
    }

    public void setMeReports(Set<MEReport> meReports) {
        this.meReports = meReports;
    }

    public Set<InspectionReport> getInspectionReports() {
        return inspectionReports;
    }

    public void setInspectionReports(Set<InspectionReport> inspectionReports) {
        this.inspectionReports = inspectionReports;
    }

    public Set<PaymentVoucher> getPaymentVouchers() {
        return paymentVouchers;
    }

    public void setPaymentVouchers(Set<PaymentVoucher> paymentVouchers) {
        this.paymentVouchers = paymentVouchers;
    }

    @AccessType(AccessType.Type.PROPERTY)
    public ZonedDateTime getLastModifiedDateInclChildren() {
        return getMax(Arrays.asList(
                getLastModifiedDate().orElse(null),
                getMaxLastModifiedDate(pmcReports),
                getMaxLastModifiedDate(meReports),
                getMaxLastModifiedDate(inspectionReports),
                getMaxLastModifiedDate(paymentVouchers),
                getMaxLastModifiedDate(administratorReports),
                getMaxLastModifiedDate(awardAcceptance),
                getMaxLastModifiedDate(awardNotification),
                getMaxLastModifiedDate(professionalOpinion),
                getMaxLastModifiedDate(tenderQuotationEvaluation),
                getMaxLastModifiedDate(tender),
                getMaxLastModifiedDate(contract)));
    }

    private ZonedDateTime getMaxLastModifiedDate(Collection<? extends AbstractAuditableEntity> col) {
        ZonedDateTime max = null;
        for (AbstractAuditableEntity e : col) {
            if (e.getLastModifiedDate().isPresent()
                    && (max == null || max.isBefore(e.getLastModifiedDate().get()))) {
                max = e.getLastModifiedDate().get();
            }
        }
        return max;
    }

    private ZonedDateTime getMax(Collection<ZonedDateTime> col) {
        ZonedDateTime max = null;
        for (ZonedDateTime e : col) {
            if (e != null && (max == null || max.isBefore(e))) {
                max = e;
            }
        }
        return max;
    }

    @SuppressWarnings("unchecked")
    public <Z extends AbstractMakueniEntity> Z getProcurementEntity(Class<Z> clazz) {
        if (clazz.equals(Project.class)) {
            return (Z) getProject();
        }
        if (clazz.equals(PurchaseRequisitionGroup.class)) {
            return (Z) getSinglePurchaseRequisition();
        }
        if (clazz.equals(Tender.class)) {
            return (Z) getSingleTender();
        }
        if (clazz.equals(TenderQuotationEvaluation.class)) {
            return (Z) getSingleTenderQuotationEvaluation();
        }
        if (clazz.equals(ProfessionalOpinion.class)) {
            return (Z) getSingleProfessionalOpinion();
        }
        if (clazz.equals(AwardNotification.class)) {
            return (Z) getSingleAwardNotification();
        }
        if (clazz.equals(AwardAcceptance.class)) {
            return (Z) getSingleAwardAcceptance();
        }
        if (clazz.equals(Contract.class)) {
            return (Z) getSingleContract();
        }
        if (clazz.equals(ProcurementPlan.class)) {
            return (Z) getProcurementPlan();
        }
        throw new RuntimeException("Unrecognized class " + clazz + " mapped to entity");
    }

    public boolean hasFormsDependingOnPurchaseRequisition() {
        return getSingleTender() != null
                || hasFormsDependingOnTender();
    }

    public boolean hasFormsDependingOnTender() {
        return getSingleTenderQuotationEvaluation() != null
                || hasFormsDependingOnTenderQuotationAndEvaluation();
    }

    public boolean hasFormsDependingOnTenderQuotationAndEvaluation() {
        return getSingleProfessionalOpinion() != null
                || hasFormsDependingOnProfessionalOpinion();
    }

    public boolean hasFormsDependingOnProfessionalOpinion() {
        return getSingleAwardNotification() != null
                || hasFormsDependingOnAwardNotification();
    }

    public boolean hasFormsDependingOnAwardNotification() {
        return getSingleAwardAcceptance() != null
                || hasFormsDependingOnAwardAcceptance();
    }

    public boolean hasFormsDependingOnAwardAcceptance() {
        return getSingleContract() != null;
    }

    public void setProcurementPlan(ProcurementPlan procurementPlan) {
        this.procurementPlan = procurementPlan;
    }

    @Override
    public void setLabel(String label) {

    }

    @Override
    public String getLabel() {
        return getId().toString();
    }
}
