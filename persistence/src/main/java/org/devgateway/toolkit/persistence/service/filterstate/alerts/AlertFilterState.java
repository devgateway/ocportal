package org.devgateway.toolkit.persistence.service.filterstate.alerts;

import org.devgateway.toolkit.persistence.dao.alerts.Alert;
import org.devgateway.toolkit.persistence.dao.alerts.Alert_;
import org.devgateway.toolkit.persistence.service.filterstate.JpaFilterState;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author idobre
 * @since 26/08/2019
 */
public class AlertFilterState extends JpaFilterState<Alert> {
    private Boolean emailVerified;

    private Boolean alertable;

    private Integer maxFailCount;

    public AlertFilterState(final Boolean emailVerified,  final Boolean alertable, final Integer maxFailCount) {
        this.emailVerified = emailVerified;
        this.alertable = alertable;
        this.maxFailCount = maxFailCount;
    }

    @Override
    public Specification<Alert> getSpecification() {
        return (root, query, cb) -> {
            final List<Predicate> predicates = new ArrayList<>();

            if (emailVerified != null) {
                predicates.add(cb.equal(root.get(Alert_.emailVerified), emailVerified));
            }

            if (alertable != null) {
                predicates.add(cb.equal(root.get(Alert_.alertable), alertable));
            }

            if (maxFailCount != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get(Alert_.failCount), maxFailCount));
            }

            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(final Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public Boolean getAlertable() {
        return alertable;
    }

    public void setAlertable(final Boolean alertable) {
        this.alertable = alertable;
    }

    public Integer getMaxFailCount() {
        return maxFailCount;
    }

    public void setMaxFailCount(final Integer maxFailCount) {
        this.maxFailCount = maxFailCount;
    }
}
