package org.devgateway.toolkit.persistence.service.alerts;

import org.devgateway.toolkit.persistence.dao.alerts.Alert;
import org.devgateway.toolkit.persistence.service.BaseJpaService;

import java.util.List;

/**
 * @author idobre
 * @since 23/08/2019
 */
public interface AlertService extends BaseJpaService<Alert> {
    Alert findBySecret(String secret);

    List<Alert> findByEmail(String email);

    void unsubscribeAlert(Alert alert);
}