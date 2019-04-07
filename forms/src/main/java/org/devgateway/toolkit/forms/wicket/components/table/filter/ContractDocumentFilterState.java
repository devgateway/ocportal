/**
 * 
 */
package org.devgateway.toolkit.forms.wicket.components.table.filter;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.devgateway.toolkit.persistence.dao.categories.ContractDocument;
import org.devgateway.toolkit.persistence.dao.categories.ContractDocument_;
import org.springframework.data.jpa.domain.Specification;

/**
 * @author gmutuhu
 *
 */
public class ContractDocumentFilterState extends JpaFilterState<ContractDocument> {
    private String label;

    @Override
    public Specification<ContractDocument> getSpecification() {
        return (root, query, cb) -> {
            final List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.isNotBlank(label)) {
                predicates.add(cb.like(cb.lower(root.get(ContractDocument_.label)), "%" + label.toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }


    public String getLabel() {
        return label;
    }

    public void setLabel(final String label) {
        this.label = label;
    }

}
