package org.devgateway.toolkit.forms.wicket.components.table.filter;

import org.apache.commons.lang3.StringUtils;
import org.devgateway.toolkit.persistence.dao.categories.ChargeAccount;
import org.devgateway.toolkit.persistence.dao.categories.ChargeAccount_;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gmutuhu
 * 
 *  */
public class ChargeAccountFilterState extends JpaFilterState<ChargeAccount> {
    private String label;

    @Override
    public Specification<ChargeAccount> getSpecification() {
        return (root, query, cb) -> {
            final List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.isNotBlank(label)) {
                predicates.add(cb.like(cb.lower(root.get(ChargeAccount_.label)), "%" + label.toLowerCase() + "%"));
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
