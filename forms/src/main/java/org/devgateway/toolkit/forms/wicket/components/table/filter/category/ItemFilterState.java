package org.devgateway.toolkit.forms.wicket.components.table.filter.category;

import org.apache.commons.lang3.StringUtils;
import org.devgateway.toolkit.persistence.dao.categories.Item;
import org.devgateway.toolkit.persistence.dao.categories.Item_;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;


public class ItemFilterState extends AbstractCategoryFilterState<Item> {
    private String itemCode;

    @Override
    public Specification<Item> getSpecification() {
        return (root, query, cb) -> {
            final List<Predicate> predicates = new ArrayList<>();   

            if (StringUtils.isNotBlank(itemCode)) {
                predicates.add(cb.like(cb.lower(root.get(Item_.itemCode).as(String.class)),
                        "%" + itemCode + "%"));
            }

            predicates.add(super.getSpecification().toPredicate(root, query, cb));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(final String itemCode) {
        this.itemCode = itemCode;
    }
}
