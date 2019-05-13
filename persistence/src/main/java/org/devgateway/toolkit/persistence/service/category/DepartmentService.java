package org.devgateway.toolkit.persistence.service.category;

import java.util.List;

import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.service.BaseJpaService;
import org.devgateway.toolkit.persistence.service.TextSearchableService;

public interface DepartmentService extends BaseJpaService<Department>, TextSearchableService<Department> {
    List<Department> findDeptsInCurrentProcurementPlan();
}
