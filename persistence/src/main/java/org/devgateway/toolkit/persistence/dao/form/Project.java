package org.devgateway.toolkit.persistence.dao.form;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.excel.annotation.ExcelExport;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @author idobre
 * @since 2019-04-02
 */
@Entity
@Audited
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(indexes = {@Index(columnList = "procurement_plan_id"), @Index(columnList = "projectTitle")})
public class Project extends AbstractMakueniEntity implements ProcurementPlanAttachable, TitleAutogeneratable {
    @ExcelExport(separateSheet = true, useTranslation = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToOne
    private CabinetPaper cabinetPaper;

    @ManyToOne(fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinColumn(name = "procurement_plan_id")
    @JsonIgnore
    @NotNull
    private ProcurementPlan procurementPlan;

    @ExcelExport(separateSheet = true, name = "Purchase Requisitions")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(mappedBy = "project")
    @JsonIgnore
    private Set<PurchaseRequisition> purchaseRequisitions = new HashSet<>();

    @ExcelExport(useTranslation = true)
    @Column(length = DBConstants.STD_DEFAULT_TEXT_LENGTH)
    private String projectTitle;

    @ExcelExport(useTranslation = true)
    private BigDecimal amountBudgeted;

    @ExcelExport(useTranslation = true)
    private BigDecimal amountRequested;

    @ExcelExport(useTranslation = true)
    private Integer numberSubCounties;

    @ExcelExport(useTranslation = true)
    private Integer numberSubWards;

    public CabinetPaper getCabinetPaper() {
        return cabinetPaper;
    }

    public void setCabinetPaper(final CabinetPaper cabinetPaper) {
        this.cabinetPaper = cabinetPaper;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public void setProjectTitle(final String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public BigDecimal getAmountBudgeted() {
        return amountBudgeted;
    }

    public void setAmountBudgeted(final BigDecimal amountBudgeted) {
        this.amountBudgeted = amountBudgeted;
    }

    public BigDecimal getAmountRequested() {
        return amountRequested;
    }

    public void setAmountRequested(final BigDecimal amountRequested) {
        this.amountRequested = amountRequested;
    }

    public Integer getNumberSubCounties() {
        return numberSubCounties;
    }

    public void setNumberSubCounties(final Integer numberSubCounties) {
        this.numberSubCounties = numberSubCounties;
    }

    public Integer getNumberSubWards() {
        return numberSubWards;
    }

    public void setNumberSubWards(final Integer numberSubWards) {
        this.numberSubWards = numberSubWards;
    }

    @Override
    public void setLabel(final String label) {

    }

    @Override
    public String getLabel() {
        return projectTitle;
    }

    @Override
    public String toString() {
        return getLabel();
    }

    @Override
    public ProcurementPlan getProcurementPlan() {
        return procurementPlan;
    }

    public void setProcurementPlan(ProcurementPlan procurementPlan) {
        this.procurementPlan = procurementPlan;
    }

    public Set<PurchaseRequisition> getPurchaseRequisitions() {
        return purchaseRequisitions;
    }

    public void setPurchaseRequisitions(Set<PurchaseRequisition> purchaseRequisitions) {
        this.purchaseRequisitions = purchaseRequisitions;
    }

    @Override
    @Transactional
    public Collection<? extends AbstractMakueniEntity> getDirectChildrenEntities() {
        return purchaseRequisitions;
    }

    @Override
    public String getTitle() {
        return getProjectTitle();
    }

    @Override
    public Consumer<String> titleSetter() {
        return this::setProjectTitle;
    }
}
