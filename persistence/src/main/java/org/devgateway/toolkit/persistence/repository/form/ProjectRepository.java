package org.devgateway.toolkit.persistence.repository.form;

import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.dao.form.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author idobre
 * @since 2019-04-02
 */
@Transactional
public interface ProjectRepository extends AbstractMakueniEntityRepository<Project> {
    @Override
    @Query("select project from  #{#entityName} project where lower(project.projectTitle) like %:name%")
    Page<Project> searchText(@Param("name") String name, Pageable page);

    Long countByProcurementPlanAndProjectTitleAndIdNot(ProcurementPlan procurementPlan, String projectTitle, Long id);

    @Override
    @Query("select project from  #{#entityName} project where project.procurementPlan.fiscalYear = :fiscalYear")
    List<Project> findByFiscalYear(@Param("fiscalYear") FiscalYear fiscalYear);

    List<Project> findByProcurementPlan(ProcurementPlan procurementPlan);
}
