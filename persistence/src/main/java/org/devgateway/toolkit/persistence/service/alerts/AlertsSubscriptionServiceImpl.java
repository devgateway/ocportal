package org.devgateway.toolkit.persistence.service.alerts;

import org.devgateway.toolkit.persistence.dao.alerts.AlertsSubscription;
import org.devgateway.toolkit.persistence.dao.categories.Subcounty;
import org.devgateway.toolkit.persistence.dao.categories.Ward;
import org.devgateway.toolkit.persistence.repository.alerts.AlertsSubscriptionRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.devgateway.toolkit.persistence.service.BaseJpaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Octavian Ciubotaru
 */
@Service
@Transactional(readOnly = true)
public class AlertsSubscriptionServiceImpl
        extends BaseJpaServiceImpl<AlertsSubscription> implements AlertsSubscriptionService {

    @Autowired
    private AlertsSubscriptionRepository repository;

    @Override
    protected BaseJpaRepository<AlertsSubscription, Long> repository() {
        return repository;
    }

    @Override
    @Transactional
    public void subscribe(String msisdn, Ward ward) {
        AlertsSubscription sub = getOrCreate(msisdn);

        if (!sub.getSubcounties().contains(ward.getSubcounty())
                && !sub.getWards().contains(ward)) {
            sub.getWards().add(ward);
        }
    }

    @Override
    @Transactional
    public void subscribe(String msisdn, Subcounty subcounty) {
        AlertsSubscription sub = getOrCreate(msisdn);

        if (!sub.getSubcounties().contains(subcounty)) {
            sub.getSubcounties().add(subcounty);
            sub.getWards().removeIf(w -> w.getSubcounty().equals(subcounty));
        }
    }

    private AlertsSubscription getOrCreate(String msisdn) {
        AlertsSubscription subscription = repository.getByMsisdn(msisdn);
        if (subscription == null) {
            subscription = new AlertsSubscription();
            subscription.setMsisdn(msisdn);
            repository.save(subscription);
        }
        return subscription;
    }

    @Override
    @Transactional
    public void unsubscribe(String msisdn) {
        AlertsSubscription sub = repository.getByMsisdn(msisdn);

        sub.getWards().clear();
        sub.getSubcounties().clear();
    }

    @Override
    @Transactional
    public void unsubscribe(String msisdn, Ward ward) {
        AlertsSubscription sub = repository.getByMsisdn(msisdn);

        sub.getWards().remove(ward);
    }

    @Override
    @Transactional
    public void unsubscribe(String msisdn, Subcounty subcounty) {
        AlertsSubscription sub = repository.getByMsisdn(msisdn);

        sub.getSubcounties().remove(subcounty);
    }

    @Override
    public AlertsSubscription getByMsisdn(String msisdn) {
        return repository.getByMsisdn(msisdn);
    }

    @Override
    @Transactional
    public void changeLanguage(String msisdn, String language) {
        AlertsSubscription sub = getOrCreate(msisdn);

        sub.setLanguage(language);
    }

    @Override
    public AlertsSubscription newInstance() {
        return new AlertsSubscription();
    }
}
