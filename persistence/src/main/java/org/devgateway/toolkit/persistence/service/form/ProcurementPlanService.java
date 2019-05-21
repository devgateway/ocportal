package org.devgateway.toolkit.persistence.service.form;

import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.service.TextSearchableService;

/**
 * @author idobre
 * @since 2019-04-02
 */
public interface ProcurementPlanService
        extends AbstractMakueniEntityService<ProcurementPlan>, TextSearchableService<ProcurementPlan> {

    Long countByDepartmentAndFiscalYearAndIdNot(Department department, FiscalYear fiscalYear, Long id);

    ProcurementPlan findByDepartmentAndFiscalYear(Department department, FiscalYear fiscalYear);
}
