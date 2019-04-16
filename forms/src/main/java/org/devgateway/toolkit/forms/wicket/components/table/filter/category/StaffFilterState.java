package org.devgateway.toolkit.forms.wicket.components.table.filter.category;

import org.apache.commons.lang3.StringUtils;
import org.devgateway.toolkit.persistence.dao.categories.Staff;
import org.devgateway.toolkit.persistence.dao.categories.Staff_;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;


public class StaffFilterState extends AbstractCategoryFilterState<Staff> {
    private String title;
    @Override
    public Specification<Staff> getSpecification() {
        return (root, query, cb) -> {
            final List<Predicate> predicates = new ArrayList<>();   

            if (StringUtils.isNotBlank(title)) {
                predicates.add(cb.like(cb.lower(root.get(Staff_.title).as(String.class)),
                        "%" + title.toLowerCase() + "%"));
            }            

            predicates.add(super.getSpecification().toPredicate(root, query, cb));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    public final String getTitle() {
        return title;
    }

    public final void setTitle(final String title) {
        this.title = title;
    }       
}
