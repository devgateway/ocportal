package org.devgateway.toolkit.forms.wicket.components.table.filter.category;

import org.devgateway.toolkit.persistence.dao.categories.TargetGroup;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author idobre
 * @since 2019-03-11
 */
public class TargetGroupFilterState extends AbstractCategoryFilterState<TargetGroup> {
    @Override
    public Specification<TargetGroup> getSpecification() {
        return (root, query, cb) -> {
            final List<Predicate> predicates = new ArrayList<>();

            predicates.add(super.getSpecification().toPredicate(root, query, cb));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
}
