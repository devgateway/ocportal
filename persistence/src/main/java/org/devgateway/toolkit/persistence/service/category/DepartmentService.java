package org.devgateway.toolkit.persistence.service.category;

import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.service.BaseJpaService;
import org.devgateway.toolkit.persistence.service.TextSearchableService;

import java.util.List;

public interface DepartmentService extends BaseJpaService<Department>, TextSearchableService<Department> {
    List<Department> findActiveDepartmentsInFiscalYear(FiscalYear fiscalYear);
}
