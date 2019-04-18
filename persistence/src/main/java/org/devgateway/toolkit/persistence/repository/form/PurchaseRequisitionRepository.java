package org.devgateway.toolkit.persistence.repository.form;

import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisition;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author idobre
 * @since 2019-04-17
 */
@Transactional
public interface PurchaseRequisitionRepository extends BaseJpaRepository<PurchaseRequisition, Long> {
    Long countByProcurementPlanAndTitleAndIdNot(ProcurementPlan procurementPlan, String title, Long id);
}
