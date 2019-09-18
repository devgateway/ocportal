package org.devgateway.toolkit.persistence.service.alerts;

import org.devgateway.toolkit.persistence.dao.alerts.Alert;
import org.devgateway.toolkit.persistence.repository.alerts.AlertRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.devgateway.toolkit.persistence.service.BaseJpaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author idobre
 * @since 23/08/2019
 */
@Service
@Transactional(readOnly = true)
public class AlertServiceImpl extends BaseJpaServiceImpl<Alert> implements AlertService {
    @Autowired
    private AlertRepository alertRepository;

    @Override
    protected BaseJpaRepository<Alert, Long> repository() {
        return alertRepository;
    }

    @Override
    public Alert findBySecret(final String secret) {
        return alertRepository.findBySecret(secret);
    }

    @Override
    public List<Alert> findByEmail(final String email) {
        return alertRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public void unsubscribeAlert(final Alert alert) {
        alert.setAlertable(false);
        alertRepository.saveAndFlush(alert);
    }

    @Override
    public Alert newInstance() {
        return new Alert();
    }
}

