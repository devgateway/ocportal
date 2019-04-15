package org.devgateway.toolkit.persistence.service.form;

import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.repository.form.ProcurementPlanRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.devgateway.toolkit.persistence.repository.norepository.TextSearchableRepository;
import org.devgateway.toolkit.persistence.service.BaseJpaServiceImpl;
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
public class ProcurementPlanServiceImpl extends BaseJpaServiceImpl<ProcurementPlan> implements ProcurementPlanService {
    @Autowired
    private ProcurementPlanRepository procurementPlanRepository;

    @Cacheable
    public Long countByDepartmentAndFiscalYearAndIdNot(final Department department,
                                                       final FiscalYear fiscalYear,
                                                       final Long id) {
        return procurementPlanRepository.countByDepartmentAndFiscalYearAndIdNot(department, fiscalYear, id);
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
}
