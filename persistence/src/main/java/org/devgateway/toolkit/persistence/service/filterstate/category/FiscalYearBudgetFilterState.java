package org.devgateway.toolkit.persistence.service.filterstate.category;

import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.dao.form.FiscalYearBudget;
import org.devgateway.toolkit.persistence.dao.form.FiscalYearBudget_;
import org.devgateway.toolkit.persistence.service.filterstate.JpaFilterState;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mpostelnicu
 */
public class FiscalYearBudgetFilterState extends JpaFilterState<FiscalYearBudget> {

    private FiscalYear fiscalYear;
    private Department department;

    @Override
    public Specification<FiscalYearBudget> getSpecification() {
        return (root, query, cb) -> {
            final List<Predicate> predicates = new ArrayList<>();

            if (!ObjectUtils.isEmpty(fiscalYear)) {
                predicates.add(cb.equal(root.get(FiscalYearBudget_.fiscalYear), fiscalYear));
            }

            if (!ObjectUtils.isEmpty(department)) {
                predicates.add(cb.equal(root.get(FiscalYearBudget_.department), department));
            }
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

}

