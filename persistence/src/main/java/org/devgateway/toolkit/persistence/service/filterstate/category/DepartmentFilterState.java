package org.devgateway.toolkit.persistence.service.filterstate.category;

import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author idobre
 * @since 2019-03-11
 */
public class DepartmentFilterState extends AbstractCategoryFilterState<Department> {
    @Override
    public Specification<Department> getSpecification() {
        return (root, query, cb) -> {
            final List<Predicate> predicates = new ArrayList<>();
            predicates.add(super.getSpecification().toPredicate(root, query, cb));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
}
