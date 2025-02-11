package org.devgateway.toolkit.persistence.service.filterstate.form;

import org.devgateway.toolkit.persistence.dao.categories.Supplier;
import org.devgateway.toolkit.persistence.dao.form.AwardNotification;
import org.devgateway.toolkit.persistence.dao.form.AwardNotificationItem_;
import org.devgateway.toolkit.persistence.dao.form.AwardNotification_;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gmutuhu
 */
public class AwardNotificationFilterState extends AbstractTenderProcessClientFilterState<AwardNotification> {
    protected Supplier awardee;

    @Override
    public Specification<AwardNotification> getSpecification() {
        return (root, query, cb) -> {
            final List<Predicate> predicates = new ArrayList<>();

            if (awardee != null) {
                predicates.add(
                        cb.equal(root.join(AwardNotification_.items).get(AwardNotificationItem_.awardee), awardee));
            }

            predicates.add(super.getSpecification().toPredicate(root, query, cb));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }


}
