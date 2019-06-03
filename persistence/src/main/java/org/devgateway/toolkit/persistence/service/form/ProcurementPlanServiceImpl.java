package org.devgateway.toolkit.persistence.service.form;

import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.repository.form.ProcurementPlanRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.devgateway.toolkit.persistence.repository.norepository.TextSearchableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author idobre
 * @since 2019-04-02
 */
@Service
@Transactional(readOnly = true)
public class ProcurementPlanServiceImpl extends AbstractMakueniEntityServiceImpl<ProcurementPlan>
        implements ProcurementPlanService {
    @Autowired
    private ProcurementPlanRepository procurementPlanRepository;

    @Cacheable
    @Override
    public Long countByDepartmentAndFiscalYear(final Department department, final FiscalYear fiscalYear) {
        return procurementPlanRepository.countByDepartmentAndFiscalYear(department, fiscalYear);
    }

    @Override
    protected BaseJpaRepository<ProcurementPlan, Long> repository() {
        return procurementPlanRepository;
    }

    @Override
    public TextSearchableRepository<ProcurementPlan, Long> textRepository() {
        return procurementPlanRepository;
    }

    @Override
    public ProcurementPlan newInstance() {
        return new ProcurementPlan();
    }

    @Override
    @Cacheable
    public ProcurementPlan findByDepartmentAndFiscalYear(final Department department, final FiscalYear fiscalYear) {
        return procurementPlanRepository.findByDepartmentAndFiscalYear(department, fiscalYear);
    }
}
