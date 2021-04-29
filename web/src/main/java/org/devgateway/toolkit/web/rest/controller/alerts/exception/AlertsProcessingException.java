package org.devgateway.toolkit.web.rest.controller.alerts.exception;

/**
 * @author idobre
 * @since 2019-08-21
 * <p>
 * Exception thrown when an error occured during user alerts processing.
 */
public class AlertsProcessingException extends Exception {

    public AlertsProcessingException(final Long alertId) {
        this(alertId, null);
    }

    public AlertsProcessingException(final Long alertId, final Throwable cause) {
        super("Couldn't process alerts for #" + alertId, cause);
    }
}
