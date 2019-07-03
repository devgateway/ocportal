package org.devgateway.toolkit.persistence.service.filterstate.form;

import org.apache.commons.lang3.StringUtils;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan_;
import org.devgateway.toolkit.persistence.dao.form.Project;
import org.devgateway.toolkit.persistence.dao.form.Project_;
import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisition;
import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisition_;
import org.devgateway.toolkit.persistence.service.filterstate.StatusAuditableEntityFilterState;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author idobre
 * @since 2019-04-17
 */
public class PurchaseRequisitionFilterState extends StatusAuditableEntityFilterState<PurchaseRequisition> {
    private Project project;

    private String title;

    @Override
    public Specification<PurchaseRequisition> getSpecification() {
        return (root, query, cb) -> {
            final List<Predicate> predicates = new ArrayList<>();

            if (project != null) {
                final ProcurementPlan procurementPlan = project.getProcurementPlan();

                if (procurementPlan != null) {
                    if (procurementPlan.getDepartment() != null) {
                        predicates.add(cb.equal(root.get(PurchaseRequisition_.project).get(Project_.procurementPlan)
                                .get(ProcurementPlan_.department), procurementPlan.getDepartment()));
                    }

                    if (procurementPlan.getFiscalYear() != null) {
                        predicates.add(cb.equal(root.get(PurchaseRequisition_.project).get(Project_.procurementPlan)
                                .get(ProcurementPlan_.fiscalYear), procurementPlan.getFiscalYear()));
                    }
                }

                if (StringUtils.isNotBlank(title)) {
                    predicates.add(cb.like(
                            cb.lower(root.get(PurchaseRequisition_.title)), "%" + title.toLowerCase() + "%"));
                }
            }

            predicates.add(super.getSpecification().toPredicate(root, query, cb));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    public Project getProject() {
        return project;
    }

    public void setProject(final Project project) {
        this.project = project;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
