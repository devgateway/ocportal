package org.devgateway.toolkit.persistence.service.filterstate.category;

import org.apache.commons.lang3.StringUtils;
import org.devgateway.toolkit.persistence.service.filterstate.JpaFilterState;
import org.devgateway.toolkit.persistence.dao.categories.Category;
import org.devgateway.toolkit.persistence.dao.categories.Category_;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author idobre
 * @since 2019-04-08
 */
public class AbstractCategoryFilterState<T extends Category> extends JpaFilterState<T> {
    private String label;
    private String code;

    @Override
    public Specification<T> getSpecification() {
        return (root, query, cb) -> {
            final List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.isNotBlank(label)) {
                predicates.add(cb.like(cb.lower(root.get(Category_.label)), "%" + label.toLowerCase() + "%"));
            }
            
            if (StringUtils.isNotBlank(code)) {
                predicates.add(cb.like(cb.lower(root.get(Category_.code)), "%" + code.toLowerCase() + "%"));
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
    
    public final String getCode() {
        return code;
    }

    public final void setCode(final String code) {
        this.code = code;
    }
}
