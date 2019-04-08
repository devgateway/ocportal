package org.devgateway.toolkit.forms.wicket.components.table.filter.category;

import org.devgateway.toolkit.persistence.dao.categories.ChargeAccount;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gmutuhu
 * 
 *  */
public class ChargeAccountFilterState extends AbstractCategoryFilterState<ChargeAccount> {
    @Override
    public Specification<ChargeAccount> getSpecification() {
        return (root, query, cb) -> {
            final List<Predicate> predicates = new ArrayList<>();

            predicates.add(super.getSpecification().toPredicate(root, query, cb));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
}
