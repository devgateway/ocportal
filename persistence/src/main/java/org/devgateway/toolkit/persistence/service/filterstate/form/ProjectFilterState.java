package org.devgateway.toolkit.persistence.service.filterstate.form;

import org.apache.commons.lang3.StringUtils;
import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan_;
import org.devgateway.toolkit.persistence.dao.form.Project;
import org.devgateway.toolkit.persistence.dao.form.Project_;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author idobre
 * @since 2019-04-02
 */
public class ProjectFilterState extends AbstractMakueniEntityFilterState<Project> {
    private FiscalYear fiscalYear;
    private String projectTitle;
    private Long procurementPlanId;
    
   public ProjectFilterState(final FiscalYear fiscalYear, final String projectTitle) {
        this.fiscalYear = fiscalYear;
        this.projectTitle = projectTitle;
    }
    
    public ProjectFilterState(final Long procurementPlanId, final String projectTitle) {
        this.procurementPlanId = procurementPlanId;
        this.projectTitle = projectTitle;
    }
    

    public ProjectFilterState() {

    }

    @Override
    public Specification<Project> getSpecification() {
        return (root, query, cb) -> {
            final List<Predicate> predicates = new ArrayList<>();

            if (fiscalYear != null) {
                predicates.add(cb.equal(
                        root.get(Project_.procurementPlan).get(ProcurementPlan_.fiscalYear), fiscalYear));
            }
            
            if (fiscalYear == null) {
                predicates.add(cb.equal(
                        root.get(Project_.procurementPlan).get(ProcurementPlan_.id), procurementPlanId));
            }      

            if (StringUtils.isNotBlank(projectTitle)) {
                predicates.add(cb.like(
                        cb.lower(root.get(Project_.projectTitle)), "%" + projectTitle.toLowerCase() + "%"));
            }

            predicates.add(super.getSpecification().toPredicate(root, query, cb));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    public FiscalYear getFiscalYear() {
        return fiscalYear;
    }

    public void setFiscalYear(final FiscalYear fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public void setProjectTitle(final String projectTitle) {
        this.projectTitle = projectTitle;
    }
    
    public Long getProcurementPlanId() {
        return procurementPlanId;
    }

    public void setProcurementPlanId(final Long procurementPlanId) {
        this.procurementPlanId = procurementPlanId;
    }
   
}
