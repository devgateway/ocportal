package org.devgateway.toolkit.forms.exceptions;

import org.devgateway.toolkit.persistence.dao.alerts.Alert;

/**
 * @author idobre
 * @since 2019-08-21
 * <p>
 * Exception thrown when an error occured during user alerts processing.
 */
public class AlertsProcessingException extends Exception {
    private Alert alert;

    public AlertsProcessingException(Alert alert, Throwable cause) {
        super("Couldn't process alerts for: " + alert.getEmail(), cause);
        this.alert = alert;
    }
}
