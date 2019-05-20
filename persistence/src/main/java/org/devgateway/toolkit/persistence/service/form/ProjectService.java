package org.devgateway.toolkit.persistence.service.form;

import java.util.List;

import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.dao.form.Project;
import org.devgateway.toolkit.persistence.service.BaseJpaService;
import org.devgateway.toolkit.persistence.service.TextSearchableService;

/**
 * @author idobre
 * @since 2019-04-02
 */
public interface ProjectService extends BaseJpaService<Project>, TextSearchableService<Project> {
    Long countByProcurementPlanAndProjectTitleAndIdNot(ProcurementPlan procurementPlan, String projectTitle, Long id);

    List<Project> findByProcurementPlan(ProcurementPlan procurementPlan);

    Long countByFiscalYear(FiscalYear fiscalYear);

    Long countByDepartmentAndFiscalYear(Department department, FiscalYear fiscalYear);
}
