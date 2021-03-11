package org.devgateway.toolkit.persistence.service.form;

import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.dao.form.Project;
import org.devgateway.toolkit.persistence.dao.form.Project_;
import org.devgateway.toolkit.persistence.repository.form.ProjectRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.metamodel.SingularAttribute;
import java.util.List;

/**
 * @author idobre
 * @since 2019-04-02
 */
@Service
@Transactional
public class ProjectServiceImpl extends AbstractMakueniEntityServiceImpl<Project> implements ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    @Override
    protected BaseJpaRepository<Project, Long> repository() {
        return projectRepository;
    }

    @Override
    public Project newInstance() {
        return new Project();
    }

    @Override
    public Long countByProcurementPlanAndProjectTitleAndIdNot(final ProcurementPlan procurementPlan,
                                                              final String projectTitle,
                                                              final Long id) {
        return projectRepository.countByProcurementPlanAndProjectTitleAndIdNot(procurementPlan, projectTitle, id);
    }

    @Override
    public List<Project> findByProcurementPlan(final ProcurementPlan procurementPlan) {
        return projectRepository.findByProcurementPlan(procurementPlan);
    }

    @Override
    public Long countByFiscalYear(final FiscalYear fiscalYear) {
        return projectRepository.countByProcurementPlanFiscalYear(fiscalYear);
    }

    @Override
    public Long countByDepartmentAndFiscalYear(final Department department, final FiscalYear fiscalYear) {
        return projectRepository.countByProcurementPlanDepartmentAndProcurementPlanFiscalYear(department, fiscalYear);
    }

    @Override
    public SingularAttribute<? super Project, String> getTextAttribute() {
        return Project_.projectTitle;
    }
}
