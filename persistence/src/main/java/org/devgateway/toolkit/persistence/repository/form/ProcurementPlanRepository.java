package org.devgateway.toolkit.persistence.repository.form;

import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author idobre
 * @since 2019-04-02
 */
@Transactional
public interface ProcurementPlanRepository extends BaseJpaRepository<ProcurementPlan, Long> {
    Long countByDepartmentAndFiscalYearAndIdNot(Department department, FiscalYear fiscalYear, Long id);
}
