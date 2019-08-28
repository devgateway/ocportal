package org.devgateway.toolkit.web.rest.controller.alerts.processsing;

import org.devgateway.toolkit.persistence.dao.alerts.Alert;
import org.devgateway.toolkit.persistence.dao.alerts.AlertsStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author idobre
 * @since 26/08/2019
 */
public class AlertsThread extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(AlertsThread.class);

    // time constants
    private static final int MILLIS_IN_SECOND = 1000;
    private static final int MILLIS_IN_MINUTE = MILLIS_IN_SECOND * 60;

    // how often the status information is logged (every 500 processed alerts)
    private static final int STATUS_FREQUENCY = 500;

    private final List<Alert> alerts;

    private final AlertsManager alertsManager;

    private final AlertsStatistics threadStats;

    private final int totalAlertsToProcess;

    private int processedAlerts = 0;     // counter of processed alerts

    private float processingSpeed = -1;  // average processing performance ( alerts / sec )

    private long startTime = 0;

    private long elapsedTime = -1;  // time taken to process all alerts (in millis)

    private int errors = 0;         // number of errors

    public AlertsThread(final List<Alert> alerts, final AlertsManager alertsManager) {
        this.alerts = alerts;
        this.alertsManager = alertsManager;
        this.totalAlertsToProcess = alerts.size();
        this.threadStats = new AlertsStatistics();
    }

    public void run() {
        logger.info(getName() + " started processing " + alerts.size() + " alert(s).");

        this.startTime = System.currentTimeMillis();

        processAlerts();

        this.elapsedTime = (int) getUpTime();
        int mins = ((int) this.elapsedTime) / MILLIS_IN_MINUTE;
        int seconds = ((int) this.elapsedTime % (MILLIS_IN_MINUTE)) / MILLIS_IN_SECOND;

        // number of alerts processed per second (in average)
        this.processingSpeed = ((float) this.processedAlerts) / (this.elapsedTime / MILLIS_IN_SECOND);

        // log last time the status
        updateStatus();
        logCurrentStatus();
        logger.info(getName() + " finished in " + mins + " minutes " + seconds + " seconds. ");
    }

    /**
     * Process Alerts one by one.
     */
    private void processAlerts() {
        for (final Alert alert : alerts) {
            this.processedAlerts++;

            try {
                final AlertsStatistics stats = alertsManager.processAlert(alert);

                // add to global thread statistics data
                this.threadStats.addStats(stats);
            } catch (Exception e) {
                threadStats.setNumberErrors(++this.errors);
                logger.error(getName()
                        + " : Couldn't process alert: " + alert.getId() + " - email: " + alert.getEmail(), e);
            }

            // update and log the status
            if (processedAlerts >= STATUS_FREQUENCY && processedAlerts % STATUS_FREQUENCY == 0) {
                updateStatus();
                logCurrentStatus();
            }
        }
    }

    /**
     * Log current status of the thread.
     */
    private void logCurrentStatus() {
        final StringBuffer buf = new StringBuffer();
        buf.append(getName());
        buf.append(" processed ").append(getDonePercentage()).append("% of alerts. ");
        buf.append("Processing speed is ");
        buf.append(this.processingSpeed).append(" alerts/s. ");
        buf.append(this.errors).append(" error(s). ");
        buf.append(this.threadStats.getNumberSentAlerts()).append(" alerts sent so far. ");
        buf.append(totalAlertsToProcess - processedAlerts).append(" alerts(s) left. ");
        logger.info(buf.toString());
    }

    /**
     * Calculates percentage of processed alerts.
     */
    private float getDonePercentage() {
        return (((float) processedAlerts) / totalAlertsToProcess) * 100;
    }

    /**
     * For how long this thread exists ?
     */
    private long getUpTime() {
        return System.currentTimeMillis() - startTime;
    }

    private void updateStatus() {
        this.processingSpeed = ((float) processedAlerts) / (getUpTime() / MILLIS_IN_SECOND);
    }

    public AlertsStatistics getThreadStats() {
        return threadStats;
    }
}
