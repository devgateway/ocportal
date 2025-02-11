package org.devgateway.toolkit.persistence.service.filterstate.category;

import org.devgateway.toolkit.persistence.dao.categories.SubWard;
import org.devgateway.toolkit.persistence.dao.categories.SubWard_;
import org.devgateway.toolkit.persistence.dao.categories.Ward;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mpostelnicu
 */
public class SubWardFilterState extends GenericCategoryFilterState<SubWard> {
    private Ward ward;

    @Override
    public Specification<SubWard> getSpecification() {
        return (root, query, cb) -> {
            final List<Predicate> predicates = new ArrayList<>();

            if (ward != null) {
                predicates.add(cb.equal(root.get(SubWard_.ward), ward));
            }

            predicates.add(super.getSpecification().toPredicate(root, query, cb));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    public Ward getWard() {
        return ward;
    }

    public void setWard(Ward ward) {
        this.ward = ward;
    }
}
