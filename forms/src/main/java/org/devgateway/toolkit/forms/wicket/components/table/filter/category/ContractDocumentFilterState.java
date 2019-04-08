/**
 * 
 */
package org.devgateway.toolkit.forms.wicket.components.table.filter.category;

import org.devgateway.toolkit.persistence.dao.categories.ContractDocument;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gmutuhu
 *
 */
public class ContractDocumentFilterState extends AbstractCategoryFilterState<ContractDocument> {
    @Override
    public Specification<ContractDocument> getSpecification() {
        return (root, query, cb) -> {
            final List<Predicate> predicates = new ArrayList<>();

            predicates.add(super.getSpecification().toPredicate(root, query, cb));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
}
