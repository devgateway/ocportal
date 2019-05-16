package org.devgateway.toolkit.forms.wicket.components.table.filter.form;

import org.devgateway.toolkit.persistence.dao.form.Project;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author idobre
 * @since 2019-04-02
 */
public class ProjectFilterState extends AbstractMakueniEntityFilterState<Project> {
    @Override
    public Specification<Project> getSpecification() {
        return (root, query, cb) -> {
            final List<Predicate> predicates = new ArrayList<>();

            predicates.add(super.getSpecification().toPredicate(root, query, cb));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
}
