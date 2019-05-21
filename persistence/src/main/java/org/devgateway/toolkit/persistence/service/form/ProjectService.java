package org.devgateway.toolkit.persistence.service.form;

import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.dao.form.Project;
import org.devgateway.toolkit.persistence.service.TextSearchableService;

import java.util.List;

/**
 * @author idobre
 * @since 2019-04-02
 */
public interface ProjectService extends AbstractMakueniEntityService<Project>, TextSearchableService<Project> {
    Long countByProcurementPlanAndProjectTitleAndIdNot(ProcurementPlan procurementPlan, String projectTitle, Long id);

    List<Project> findByProcurementPlan(ProcurementPlan procurementPlan);

    Long countByFiscalYear(FiscalYear fiscalYear);

    Long countByDepartmentAndFiscalYear(Department department, FiscalYear fiscalYear);
}
