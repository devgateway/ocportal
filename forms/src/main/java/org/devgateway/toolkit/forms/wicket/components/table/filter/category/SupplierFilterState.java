package org.devgateway.toolkit.forms.wicket.components.table.filter.category;

import org.apache.commons.lang3.StringUtils;
import org.devgateway.toolkit.persistence.dao.categories.Supplier;
import org.devgateway.toolkit.persistence.dao.categories.Supplier_;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;


public class SupplierFilterState extends AbstractCategoryFilterState<Supplier> {
    private String code;

    @Override
    public Specification<Supplier> getSpecification() {
        return (root, query, cb) -> {
            final List<Predicate> predicates = new ArrayList<>();   

            if (StringUtils.isNotBlank(code)) {
                predicates.add(cb.like(cb.lower(root.get(Supplier_.code).as(String.class)),
                        "%" + code + "%"));
            }

            predicates.add(super.getSpecification().toPredicate(root, query, cb));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    public final String getCode() {
        return code;
    }

    public final void setCode(final String code) {
        this.code = code;
    }

   
}
