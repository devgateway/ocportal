package org.devgateway.toolkit.persistence.service.overview;

import org.devgateway.toolkit.persistence.dto.StatusOverviewData;

import java.util.List;

/**
 * @author gmutuhu
 */
public interface StatusOverviewService {
    List<StatusOverviewData> getProjectsByDepartment(Long fiscaYearId);
}
