package org.devgateway.toolkit.forms.wicket.components.table.filter.form;

import org.devgateway.toolkit.forms.wicket.components.table.filter.StatusAuditableEntityFilterState;
import org.devgateway.toolkit.persistence.dao.form.AbstractMakueniEntity;
import org.devgateway.toolkit.persistence.dao.form.AbstractMakueniEntity_;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan_;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author idobre
 * @since 2019-04-02
 */
public abstract class AbstractMakueniEntityFilterState<T extends AbstractMakueniEntity>
        extends StatusAuditableEntityFilterState<T> {
    private ProcurementPlan procurementPlan;

    @Override
    public Specification<T> getSpecification() {
        return (root, query, cb) -> {
            final List<Predicate> predicates = new ArrayList<>();

            // TODO
            // if (procurementPlan != null) {
            //     if (procurementPlan.getDepartment() != null) {
            //         predicates.add(cb.equal(
            //                 root.get(AbstractMakueniEntity_.procurementPlan).get(ProcurementPlan_.department),
            //                 procurementPlan.getDepartment()));
            //     }
            //
            //     if (procurementPlan.getFiscalYear() != null) {
            //         predicates.add(cb.equal(
            //                 root.get(AbstractMakueniEntity_.procurementPlan).get(ProcurementPlan_.fiscalYear),
            //                 procurementPlan.getFiscalYear()));
            //     }
            // }

            predicates.add(super.getSpecification().toPredicate(root, query, cb));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    public ProcurementPlan getProcurementPlan() {
        return procurementPlan;
    }

    public void setProcurementPlan(final ProcurementPlan procurementPlan) {
        this.procurementPlan = procurementPlan;
    }
}
