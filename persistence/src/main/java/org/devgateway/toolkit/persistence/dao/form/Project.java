package org.devgateway.toolkit.persistence.dao.form;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author idobre
 * @since 2019-04-02
 */
@Entity
@Audited
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(indexes = {@Index(columnList = "procurement_plan_id"), @Index(columnList = "projectTitle")})
public class Project extends AbstractMakueniEntity {
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToOne
    private CabinetPaper cabinetPaper;

    @ManyToOne(fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinColumn(name = "procurement_plan_id")
    @JsonIgnore
    private ProcurementPlan procurementPlan;


    @Column(length = DBConstants.STD_DEFAULT_TEXT_LENGTH)
    private String projectTitle;

    private Double amountBudgeted;

    private Double amountRequested;

    private Integer numberSubCounties;

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

    public Double getAmountBudgeted() {
        return amountBudgeted;
    }

    public void setAmountBudgeted(final Double amountBudgeted) {
        this.amountBudgeted = amountBudgeted;
    }

    public Double getAmountRequested() {
        return amountRequested;
    }

    public void setAmountRequested(final Double amountRequested) {
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

    public ProcurementPlan getProcurementPlan() {
        return procurementPlan;
    }

    public void setProcurementPlan(ProcurementPlan procurementPlan) {
        this.procurementPlan = procurementPlan;
    }
}
