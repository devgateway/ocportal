package org.devgateway.toolkit.persistence.service.filterstate.prequalification;

import org.apache.commons.lang3.StringUtils;
import org.devgateway.toolkit.persistence.dao.TestForm;
import org.devgateway.toolkit.persistence.dao.TestForm_;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationSchema;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationSchema_;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationYearRange;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationYearRange_;
import org.devgateway.toolkit.persistence.service.filterstate.StatusAuditableEntityFilterState;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class PrequalificationSchemaFilterState extends StatusAuditableEntityFilterState<PrequalificationSchema> {
    private String name;
    private String prefix;
    private PrequalificationYearRange prequalificationYearRanges;

    @Override
    public Specification<PrequalificationSchema> getSpecification() {
        return (root, query, cb) -> {
            final List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.isNotBlank(name)) {
                predicates.add(cb.like(root.get(PrequalificationSchema_.name), "%" + name + "%"));
            }

            if (StringUtils.isNotBlank(prefix)) {
                predicates.add(cb.like(root.get(PrequalificationSchema_.prefix), "%" + prefix + "%"));
            }

            if (prequalificationYearRanges != null) {
                return subSetIn(root, query, PrequalificationSchema.class,
                        PrequalificationSchema_.prequalificationYearRanges,
                        PrequalificationYearRange_.id, PrequalificationYearRange::getId,
                        Collections.singleton(prequalificationYearRanges));
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

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
