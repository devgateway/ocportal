package org.devgateway.toolkit.persistence.repository.form;

import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.dao.form.Project;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author idobre
 * @since 2019-04-02
 */
@Transactional
public interface ProjectRepository extends AbstractClientEntityRepository<Project> {

    Long countByProcurementPlanAndProjectTitleAndIdNot(ProcurementPlan procurementPlan, String projectTitle, Long id);

    @Override
    @Query("select project from  #{#entityName} project where project.procurementPlan.fiscalYear = :fiscalYear")
    List<Project> findByFiscalYear(@Param("fiscalYear") FiscalYear fiscalYear);

    List<Project> findByProcurementPlan(ProcurementPlan procurementPlan);

    Long countByProcurementPlanFiscalYear(FiscalYear fiscalYear);

    Long countByProcurementPlanDepartmentAndProcurementPlanFiscalYear(Department department,
                                                                      FiscalYear fiscalYear);
}
