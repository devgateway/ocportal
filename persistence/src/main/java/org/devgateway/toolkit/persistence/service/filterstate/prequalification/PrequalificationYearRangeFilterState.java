package org.devgateway.toolkit.persistence.service.filterstate.prequalification;

import org.apache.commons.lang3.StringUtils;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationSchema;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationYearRange;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationYearRange_;
import org.devgateway.toolkit.persistence.service.filterstate.JpaFilterState;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class PrequalificationYearRangeFilterState extends JpaFilterState<PrequalificationYearRange> {
    private String name;
    private PrequalificationSchema schema;

    @Override
    public Specification<PrequalificationYearRange> getSpecification() {
        return (root, query, cb) -> {
            final List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.isNotBlank(name)) {
                predicates.add(cb.like(cb.lower(root.get(PrequalificationYearRange_.name)),
                        "%" + name.toLowerCase() + "%"));
            }

            if (schema != null) {
                predicates.add(cb.equal(root.get(PrequalificationYearRange_.schema), schema));
            }

            predicates.add(super.getSpecification().toPredicate(root, query, cb));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public PrequalificationSchema getSchema() {
        return schema;
    }

    public void setSchema(PrequalificationSchema schema) {
        this.schema = schema;
    }
}
