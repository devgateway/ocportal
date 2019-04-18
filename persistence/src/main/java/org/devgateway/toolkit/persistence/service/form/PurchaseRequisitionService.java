package org.devgateway.toolkit.persistence.service.form;

import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisition;
import org.devgateway.toolkit.persistence.service.BaseJpaService;

/**
 * @author idobre
 * @since 2019-04-17
 */
public interface PurchaseRequisitionService extends BaseJpaService<PurchaseRequisition> {
    Long countByProcurementPlanAndTitleAndIdNot(ProcurementPlan procurementPlan, String title, Long id);
}

