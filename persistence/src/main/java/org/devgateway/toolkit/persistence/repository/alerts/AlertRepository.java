package org.devgateway.toolkit.persistence.repository.alerts;

import org.devgateway.toolkit.persistence.dao.alerts.Alert;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author idobre
 * @since 23/08/2019
 */
@Transactional
public interface AlertRepository extends BaseJpaRepository<Alert, Long> {
    Alert findBySecret(String secret);

    List<Alert> findByEmail(String email);
}
