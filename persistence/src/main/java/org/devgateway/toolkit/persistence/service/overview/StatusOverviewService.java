package org.devgateway.toolkit.persistence.service.overview;

import java.util.List;

import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.dto.DepartmentOverviewData;

/**
 * @author gmutuhu
 */
public interface StatusOverviewService {
    List<FiscalYear> getYearsWithData();
    List<DepartmentOverviewData> getProjectsByDepartment(Long fiscaYearId);
    FiscalYear getLastFiscalYear();
    List<Department> findDeptsInCurrentProcurementPlan();
}
