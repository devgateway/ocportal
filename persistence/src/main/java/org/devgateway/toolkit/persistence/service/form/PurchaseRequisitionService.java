package org.devgateway.toolkit.persistence.service.form;

import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.dao.form.Project;
import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisition;
import org.devgateway.toolkit.persistence.service.TextSearchableService;

import java.util.List;

/**
 * @author idobre
 * @since 2019-04-17
 */
public interface PurchaseRequisitionService extends AbstractMakueniEntityService<PurchaseRequisition>,
        TextSearchableService<PurchaseRequisition> {
    Long countByProjectProcurementPlanAndTitleAndIdNot(ProcurementPlan procurementPlan, String title, Long id);
    
    List<PurchaseRequisition> findByProject(Project project);
}

