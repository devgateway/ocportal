package org.devgateway.toolkit.persistence.dao.form;

import org.devgateway.toolkit.persistence.dao.FileMetadata;
import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author idobre
 * @since 2019-04-01
 */
@Entity
@Audited
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(indexes = {@Index(columnList = "department_id"), @Index(columnList = "fiscal_year_id")})
public class ProcurementPlan extends AbstractMakueniForm {
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToOne
    private Department department;

    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToOne
    private FiscalYear fiscalYear;

    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "parent_id")
    @OrderColumn(name = "index")
    private List<PlanItem> planItems = new ArrayList<>();

    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<FileMetadata> procurementPlanDocs;

    public ProcurementPlan() {

    }

    @Override
    public ProcurementPlan getProcurementPlan() {
        return this;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(final Department department) {
        this.department = department;
    }

    public FiscalYear getFiscalYear() {
        return fiscalYear;
    }

    public void setFiscalYear(final FiscalYear fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    public List<PlanItem> getPlanItems() {
        return planItems;
    }

    public void setPlanItems(final List<PlanItem> planItems) {
        this.planItems = planItems;
    }

    public Set<FileMetadata> getProcurementPlanDocs() {
        return procurementPlanDocs;
    }

    public void setProcurementPlanDocs(final Set<FileMetadata> procurementPlanDocs) {
        this.procurementPlanDocs = procurementPlanDocs;
    }
}
