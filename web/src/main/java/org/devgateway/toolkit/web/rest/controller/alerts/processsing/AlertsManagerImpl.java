package org.devgateway.toolkit.web.rest.controller.alerts.processsing;

import org.devgateway.toolkit.persistence.dao.alerts.Alert;
import org.devgateway.toolkit.persistence.dao.alerts.AlertsStatistics;
import org.devgateway.toolkit.persistence.service.alerts.AlertService;
import org.devgateway.toolkit.persistence.service.alerts.AlertsStatisticsService;
import org.devgateway.toolkit.persistence.service.filterstate.alerts.AlertFilterState;
import org.devgateway.toolkit.web.rest.controller.alerts.AlertsEmailService;
import org.devgateway.toolkit.web.rest.controller.alerts.exception.AlertsProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author idobre
 * @since 26/08/2019
 */
@Service
public class AlertsManagerImpl implements AlertsManager {
    private static final Logger logger = LoggerFactory.getLogger(AlertsManagerImpl.class);

    private static final Integer MAX_FAIL_COUNT = 3;

    private static final int THREAD_COUNT = 2;

    @Autowired
    private AlertService alertService;

    @Autowired
    private AlertsEmailService alertsEmailService;

    @Autowired
    private AlertsStatisticsService alertsStatisticsService;

    @Override
    public void sendAlerts() {
        final List<Alert> alerts = getAlertableUsers();

        logger.info(alerts.size() + " alerts(s) will be processed.");
        List<Alert>[] lists = splitList(alerts);
        logger.info(lists.length + " threads will be created.");

        // create and start alerts threads
        final List<AlertsThread> threads = new ArrayList<>();
        for (int i = 0; i < lists.length; i++) {
            final AlertsThread thread = new AlertsThread(lists[i], this);
            thread.setName("Alerts Thread #" + i);
            thread.start();
            threads.add(thread);
        }

        final AlertsStatistics globalStats = new AlertsStatistics();

        try {
            // wait for threads to finish
            for (final AlertsThread thread : threads) {
                logger.info("Waiting for thread " + thread.getName() + " to finish.");
                thread.join();

                globalStats.addStats(thread.getThreadStats());
            }
        } catch (InterruptedException e) {
            logger.error("Couldn't join all threads", e);
        } finally {
            alertsStatisticsService.saveAndFlush(globalStats);
        }
    }

    @Override
    @Transactional
    public AlertsStatistics processAlert(final Alert alert) throws AlertsProcessingException {
        try {
            final AlertsStatistics stats = new AlertsStatistics();

            if (true) {

                stats.startDbAccess();
                // get tender alert.
                stats.endDbAccess();

                stats.setNumberSentAlerts(1);

                sendAlertMessage(alert, stats);

                // update Alert...
                alert.setLastChecked(LocalDateTime.now());
                alert.setFailCount(0);
                alert.setLastSendDate(LocalDateTime.now());
                alert.setLastErrorMessage(null);
                alertService.saveAndFlush(alert);
            } else {

            }

            return stats;
        } catch (Exception e) {
            logger.error("Error processing alert: " + alert.getId(), e);

            alert.setFailCount(alert.getFailCount() + 1);
            alert.setLastErrorMessage(e.getMessage());
            alertService.saveAndFlush(alert);

            throw new AlertsProcessingException(alert, e);
        }
    }

    private void sendAlertMessage(Alert alert, AlertsStatistics stats) throws MailException {

        stats.startSendingStage();

        final SimpleMailMessage message = createMailMessage(alert);
        alertsEmailService.sendEmailAlert(alert, message);

        stats.endSendingStage();
    }

    private SimpleMailMessage createMailMessage(final Alert alert) {
        final SimpleMailMessage msg = new SimpleMailMessage();

        msg.setTo(alert.getEmail());
        msg.setFrom("noreply@dgstg.org");
        msg.setSubject("Makueni OC Portal - Notifications");

        msg.setText("Hello,\n\n"
                + "Alerts....\n\n"
                + "Thanks,\n"
                + "Makueni Portal Team");

        return msg;
    }

    /**
     * Get the list of valid {@link Alert}
     * Things take into consideration {@link Alert#getEmailVerified()}, {@link Alert#getAlertable()} ()}, etc...
     */
    private List<Alert> getAlertableUsers() {
        final AlertFilterState alertFilterState = new AlertFilterState(true, true, MAX_FAIL_COUNT);

        return alertService.findAllNoCache(alertFilterState.getSpecification());
    }

    /**
     * Splits a list into <tt>THREAD_COUNT</tt> sublists.
     *
     * @param list List to be splitted.
     * @return Array of sublists.
     */
    private List[] splitList(List list) {
        final int size = list.size();
        int subLists = THREAD_COUNT;
        int chunk = size / subLists;
        if (chunk == 0) {
            chunk = 1;
            subLists = size;
        }

        final List splitted = new ArrayList(subLists);
        for (int i = 0; i < subLists; i++) {
            // for the last sublist get all remaining elements
            final int toIndex = (i == subLists - 1) ? size : (i + 1) * chunk;
            splitted.add(list.subList(i * chunk, toIndex));
        }

        return (List[]) splitted.toArray(new List[splitted.size()]);
    }
}
