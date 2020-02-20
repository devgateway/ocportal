package org.devgateway.toolkit.persistence.service.form;

import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.dao.form.FiscalYearBudget;
import org.devgateway.toolkit.persistence.service.BaseJpaService;

/**
 * @author mpostelnicu
 */
public interface FiscalYearBudgetService extends BaseJpaService<FiscalYearBudget> {

    Long countByDepartmentAndFiscalYear(Department department, FiscalYear fiscalYear);

    FiscalYearBudget findByDepartmentAndFiscalYear(Department department, FiscalYear fiscalYear);

}
