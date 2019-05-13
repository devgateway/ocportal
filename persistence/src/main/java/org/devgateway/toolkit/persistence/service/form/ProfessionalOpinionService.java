package org.devgateway.toolkit.persistence.service.form;

import java.util.List;

import org.devgateway.toolkit.persistence.dao.form.ProfessionalOpinion;
import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisition;
import org.devgateway.toolkit.persistence.service.BaseJpaService;

/**
 * @author idobre
 * @since 2019-04-24
 */
public interface ProfessionalOpinionService extends BaseJpaService<ProfessionalOpinion> {
    
    ProfessionalOpinion findByPurchaseRequisition(PurchaseRequisition purchaseRequisition);

}
