package org.devgateway.toolkit.persistence.dao.alerts;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

/**
 * @author idobre
 * @since 26/08/2019
 */
public class AlertMessage {
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToOne
    private Alert alert;

    private LocalDateTime sendDate;

    private String subject;

    private String message;

    private Boolean send;

    private String errorMessage;
}
