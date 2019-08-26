package org.devgateway.toolkit.web.rest.controller.alerts;

import org.devgateway.toolkit.persistence.dao.alerts.Alert;
import org.devgateway.toolkit.persistence.dao.alerts.AlertsStatistics;
import org.devgateway.toolkit.persistence.service.alerts.AlertService;
import org.devgateway.toolkit.persistence.service.filterstate.alerts.AlertFilterState;
import org.devgateway.toolkit.web.rest.controller.alerts.exception.AlertsProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author idobre
 * @since 26/08/2019
 */
@Service
public class AlertsManagerImpl implements AlertsManager {
    private static final Logger logger = LoggerFactory.getLogger(AlertsManagerImpl.class);

    private static final Integer MAX_FAIL_COUNT = 3;

    @Autowired
    private AlertService alertService;

    @Override
    public void sendAlerts() {
        final List<Alert> alerts = getAlertableUsers();

        logger.info(alerts.size() + " alerts(s) will be processed.");
        // List[] lists = splitList( alert );
        // logger.info(lists.length + " threads will be created.");
    }

    @Override
    public AlertsStatistics processAlert(final Alert alert) throws AlertsProcessingException {
        return null;
    }

    @Override
    public List<Alert> getAlertsByEmail(final String email) throws AlertsProcessingException {
        return null;
    }

    /**
     * Get the list of valid {@link Alert}
     * Things take into consideration {@link Alert#getEmailVerified()}, {@link Alert#getAlertable()} ()}, etc...
     */
    private List<Alert> getAlertableUsers() {
        final AlertFilterState alertFilterState = new AlertFilterState(true, true, MAX_FAIL_COUNT);

        return alertService.findAllNoCache(alertFilterState.getSpecification());
    }
}
