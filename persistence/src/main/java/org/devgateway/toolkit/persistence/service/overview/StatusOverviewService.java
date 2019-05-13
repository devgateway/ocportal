package org.devgateway.toolkit.persistence.service.overview;

import org.devgateway.toolkit.persistence.dto.DepartmentOverviewData;

import java.util.List;

/**
 * @author gmutuhu
 */
public interface StatusOverviewService {
    List<DepartmentOverviewData> getProjectsByDepartment(Long fiscaYearId);
}
