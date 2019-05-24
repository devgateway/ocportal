package org.devgateway.toolkit.persistence.service.filterstate.form;

import org.devgateway.toolkit.persistence.dao.form.AbstractPurchaseReqMakueniEntity;
import org.devgateway.toolkit.persistence.dao.form.AbstractPurchaseReqMakueniEntity_;
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
 * @since 2019-05-23
 */

public abstract class AbstractPurchaseReqMakueniFilterState<T extends AbstractPurchaseReqMakueniEntity>
        extends StatusAuditableEntityFilterState<T> {
    private PurchaseRequisition purchaseRequisition;

    @Override
    public Specification<T> getSpecification() {
        return (root, query, cb) -> {
            final List<Predicate> predicates = new ArrayList<>();

            if (purchaseRequisition != null) {
                final Project project = purchaseRequisition.getProject();

                if (project != null) {
                    final ProcurementPlan procurementPlan = project.getProcurementPlan();

                    if (procurementPlan != null) {
                        if (procurementPlan.getDepartment() != null) {
                            predicates.add(cb.equal(root.get(AbstractPurchaseReqMakueniEntity_.purchaseRequisition)
                                    .get(PurchaseRequisition_.project).get(Project_.procurementPlan)
                                    .get(ProcurementPlan_.department), procurementPlan.getDepartment()));
                        }

                        if (procurementPlan.getFiscalYear() != null) {
                            predicates.add(cb.equal(root.get(AbstractPurchaseReqMakueniEntity_.purchaseRequisition)
                                    .get(PurchaseRequisition_.project).get(Project_.procurementPlan)
                                    .get(ProcurementPlan_.fiscalYear), procurementPlan.getFiscalYear()));
                        }
                    }
                }
            }

            predicates.add(super.getSpecification().toPredicate(root, query, cb));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    public PurchaseRequisition getPurchaseRequisition() {
        return purchaseRequisition;
    }

    public void setPurchaseRequisition(final PurchaseRequisition purchaseRequisition) {
        this.purchaseRequisition = purchaseRequisition;
    }
}

