package org.devgateway.toolkit.persistence.repository.alerts;

import org.devgateway.toolkit.persistence.dao.alerts.AlertsSubscription;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;

/**
 * @author Octavian Ciubotaru
 * @since 07/10/2020
 */
public interface AlertsSubscriptionRepository extends BaseJpaRepository<AlertsSubscription, Long> {

    AlertsSubscription getByMsisdn(String msisdn);
}
