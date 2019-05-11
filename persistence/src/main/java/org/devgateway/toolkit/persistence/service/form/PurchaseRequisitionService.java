package org.devgateway.toolkit.persistence.service.form;

import java.util.List;

import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.dao.form.Project;
import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisition;
import org.devgateway.toolkit.persistence.service.BaseJpaService;
import org.devgateway.toolkit.persistence.service.TextSearchableService;

/**
 * @author idobre
 * @since 2019-04-17
 */
public interface PurchaseRequisitionService
        extends BaseJpaService<PurchaseRequisition>, TextSearchableService<PurchaseRequisition> {
    Long countByProcurementPlanAndTitleAndIdNot(ProcurementPlan procurementPlan, String title, Long id);
    
    List<PurchaseRequisition> findByProject(Project project);
}

