package org.devgateway.toolkit.persistence.service.filterstate.category;

import org.devgateway.toolkit.persistence.dao.categories.Subcounty;
import org.devgateway.toolkit.persistence.dao.categories.Ward;
import org.devgateway.toolkit.persistence.dao.categories.Ward_;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author idobre
 * @since 2019-06-26
 */
public class WardFilterState extends AbstractCategoryFilterState<Ward> {
    private Subcounty subcounty;

    @Override
    public Specification<Ward> getSpecification() {
        return (root, query, cb) -> {
            final List<Predicate> predicates = new ArrayList<>();

            if (subcounty != null) {
                predicates.add(cb.equal(root.get(Ward_.subcounty), subcounty));
            }

            predicates.add(super.getSpecification().toPredicate(root, query, cb));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    public Subcounty getSubcounty() {
        return subcounty;
    }

    public void setSubcounty(final Subcounty subcounty) {
        this.subcounty = subcounty;
    }
}