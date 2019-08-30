package org.devgateway.toolkit.persistence.service.alerts;

import org.devgateway.toolkit.persistence.dao.alerts.AlertsStatistics;
import org.devgateway.toolkit.persistence.repository.alerts.AlertsStatisticsRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.devgateway.toolkit.persistence.service.BaseJpaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author idobre
 * @since 26/08/2019
 */
@Service
@Transactional(readOnly = true)
public class AlertsStatisticsServiceImpl
        extends BaseJpaServiceImpl<AlertsStatistics> implements AlertsStatisticsService {
    @Autowired
    private AlertsStatisticsRepository alertsStatisticsRepository;

    @Override
    protected BaseJpaRepository<AlertsStatistics, Long> repository() {
        return alertsStatisticsRepository;
    }

    @Override
    public AlertsStatistics newInstance() {
        return new AlertsStatistics();
    }
}


