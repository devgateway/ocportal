package org.devgateway.toolkit.persistence.service.form;

import org.devgateway.toolkit.persistence.dao.form.AwardAcceptance;
import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisition;
import org.devgateway.toolkit.persistence.service.BaseJpaService;

/**
 * @author gmutuhu
 */
public interface AwardAcceptanceService extends BaseJpaService<AwardAcceptance> {
    AwardAcceptance findByPurchaseRequisition(PurchaseRequisition purchaseRequisition);
}
