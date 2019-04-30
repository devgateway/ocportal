package org.devgateway.toolkit.forms.wicket.components.table.filter.form;

import org.devgateway.toolkit.persistence.dao.form.AwardNotification;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gmutuhu
 */
public class AwardNotificationFilterState extends AbstractMakueniFormFilterState<AwardNotification> {
    @Override
    public Specification<AwardNotification> getSpecification() {
        return (root, query, cb) -> {
            final List<Predicate> predicates = new ArrayList<>();

            predicates.add(super.getSpecification().toPredicate(root, query, cb));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
}
