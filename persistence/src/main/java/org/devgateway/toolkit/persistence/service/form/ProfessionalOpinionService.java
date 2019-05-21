package org.devgateway.toolkit.persistence.service.form;

import org.devgateway.toolkit.persistence.dao.form.ProfessionalOpinion;
import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisition;

/**
 * @author idobre
 * @since 2019-04-24
 */
public interface ProfessionalOpinionService extends AbstractMakueniEntityService<ProfessionalOpinion> {

    ProfessionalOpinion findByPurchaseRequisition(PurchaseRequisition purchaseRequisition);

}
