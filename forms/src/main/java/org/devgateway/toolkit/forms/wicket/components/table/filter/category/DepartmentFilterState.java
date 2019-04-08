package org.devgateway.toolkit.forms.wicket.components.table.filter.category;

import org.apache.commons.lang3.StringUtils;
import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.categories.Department_;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author idobre
 * @since 2019-03-11
 */
public class DepartmentFilterState extends AbstractCategoryFilterState<Department> {
    private String code;

    @Override
    public Specification<Department> getSpecification() {
        return (root, query, cb) -> {
            final List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.isNotBlank(code)) {
                predicates.add(cb.like(cb.lower(root.get(Department_.code).as(String.class)),
                        "%" + code + "%"));
            }

            predicates.add(super.getSpecification().toPredicate(root, query, cb));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    public String getCode() {
        return code;
    }

    public void setCode(final Integer code) {
        this.code = code.toString();
    }
}
