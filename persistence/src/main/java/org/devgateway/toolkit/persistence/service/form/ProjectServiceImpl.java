package org.devgateway.toolkit.persistence.service.form;

import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.dao.form.Project;
import org.devgateway.toolkit.persistence.repository.form.ProjectRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.devgateway.toolkit.persistence.repository.norepository.TextSearchableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author idobre
 * @since 2019-04-02
 */
@Service
@Transactional(readOnly = true)
public class ProjectServiceImpl extends AbstractMakueniEntityServiceImpl<Project> implements ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    @Override
    protected BaseJpaRepository<Project, Long> repository() {
        return projectRepository;
    }

    @Override
    public TextSearchableRepository<Project, Long> textRepository() {
        return projectRepository;
    }

    @Override
    public Project newInstance() {
        return new Project();
    }

    @Cacheable
    @Override
    public Long countByProcurementPlanAndProjectTitleAndIdNot(final ProcurementPlan procurementPlan,
                                                              final String projectTitle,
                                                              final Long id) {
        return projectRepository.countByProcurementPlanAndProjectTitleAndIdNot(procurementPlan, projectTitle, id);
    }

    @Cacheable
    @Override
    public List<Project> findByProcurementPlan(final ProcurementPlan procurementPlan) {
        return projectRepository.findByProcurementPlan(procurementPlan);
    }

    @Cacheable
    @Override
    public Long countByFiscalYear(final FiscalYear fiscalYear) {
        return projectRepository.countByProcurementPlanFiscalYear(fiscalYear);
    }

    @Cacheable
    @Override
    public Long countByDepartmentAndFiscalYear(final Department department, final FiscalYear fiscalYear) {
        return projectRepository.countByProcurementPlanDepartmentAndProcurementPlanFiscalYear(department, fiscalYear);
    }
}
