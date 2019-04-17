package org.devgateway.toolkit.persistence.repository.form;

import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.dao.form.Project;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author idobre
 * @since 2019-04-02
 */
@Transactional
public interface ProjectRepository extends BaseJpaRepository<Project, Long> {
    Long countByProcurementPlanAndProjectTitleAndIdNot(ProcurementPlan procurementPlan, String projectTitle, Long id);
}
