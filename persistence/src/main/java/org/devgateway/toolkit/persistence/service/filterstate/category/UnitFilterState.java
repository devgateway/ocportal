package org.devgateway.toolkit.persistence.service.filterstate.category;

import org.devgateway.toolkit.persistence.dao.categories.Unit;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author idobre
 * @since 26/08/2019
 */
public class UnitFilterState extends AbstractCategoryFilterState<Unit> {
    @Override
    public Specification<Unit> getSpecification() {
        return (root, query, cb) -> {
            final List<Predicate> predicates = new ArrayList<>();

            predicates.add(super.getSpecification().toPredicate(root, query, cb));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
}

