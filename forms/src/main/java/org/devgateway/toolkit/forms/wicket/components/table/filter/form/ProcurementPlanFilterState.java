package org.devgateway.toolkit.forms.wicket.components.table.filter.form;

import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
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
public class ProcurementPlanFilterState extends AbstractMakueniEntityFilterState<ProcurementPlan> {
    private Department department;

    private FiscalYear fiscalYear;

    @Override
    public Specification<ProcurementPlan> getSpecification() {
        return (root, query, cb) -> {
            final List<Predicate> predicates = new ArrayList<>();

            if (department != null) {
                predicates.add(cb.equal(root.get(ProcurementPlan_.department), department));
            }

            if (fiscalYear != null) {
                predicates.add(cb.equal(root.get(ProcurementPlan_.fiscalYear), fiscalYear));
            }

            predicates.add(super.getSpecification().toPredicate(root, query, cb));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
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
}
