package org.devgateway.toolkit.persistence.service.alerts;

import org.devgateway.toolkit.persistence.dao.alerts.AlertsSubscription;
import org.devgateway.toolkit.persistence.dao.categories.Subcounty;
import org.devgateway.toolkit.persistence.dao.categories.Ward;
import org.devgateway.toolkit.persistence.service.BaseJpaService;

/**
 * @author Octavian Ciubotaru
 */
public interface AlertsSubscriptionService extends BaseJpaService<AlertsSubscription> {

    AlertsSubscription getByMsisdn(String msisdn);

    void subscribe(String msisdn, Ward ward);

    void subscribe(String msisdn, Subcounty subcounty);

    void unsubscribe(String msisdn);

    void unsubscribe(String msisdn, Ward ward);

    void unsubscribe(String msisdn, Subcounty subcounty);

    void changeLanguage(String msisdn, String language);
}
