package org.devgateway.toolkit.persistence.dao.form;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.excel.annotation.ExcelExport;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
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
public class ProcurementPlan extends AbstractMakueniEntity {
    @ExcelExport(justExport = true, useTranslation = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToOne
    private Department department;

    @ExcelExport(justExport = true, useTranslation = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToOne
    private FiscalYear fiscalYear;

    @ExcelExport(separateSheet = true, useTranslation = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "parent_id")
    @OrderColumn(name = "index")
    private List<PlanItem> planItems = new ArrayList<>();

    @ExcelExport(separateSheet = true, name = "Projects")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(mappedBy = "procurementPlan")
    private Set<Project> projects = new HashSet<>();

    public ProcurementPlan() {

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

    public Set<Project> getProjects() {
        return projects;
    }

    public void setProjects(final Set<Project> projects) {
        this.projects = projects;
    }

    public void addProject(final Project p) {
        projects.add(p);
        p.setProcurementPlan(this);
    }

    public void removeProject(final Project p) {
        projects.remove(p);
        p.setProcurementPlan(null);
    }

    @Override
    public void setLabel(final String label) {

    }

    @Override
    @JsonIgnore
    public String getLabel() {
        String fullName = "";
        if (fiscalYear != null) {
            fullName += fiscalYear.getName();
        }

        if (department != null) {
            fullName += " - " + department.getLabel();
        }

        return fullName;

    }

    @Override
    public String toString() {
        return getLabel();
    }

    @Override
    @Transactional
    @JsonIgnore
    public Collection<? extends AbstractMakueniEntity> getDirectChildrenEntities() {
        return Collections.emptyList();
    }
}
