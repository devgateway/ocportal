package org.devgateway.toolkit.persistence.repository.alerts;

import org.devgateway.toolkit.persistence.dao.alerts.AlertsStatistics;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author idobre
 * @since 26/08/2019
 */
@Transactional
public interface AlertsStatisticsRepository extends BaseJpaRepository<AlertsStatistics, Long> {

}
