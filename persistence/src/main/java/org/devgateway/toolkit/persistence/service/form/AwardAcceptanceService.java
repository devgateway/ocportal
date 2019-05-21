package org.devgateway.toolkit.persistence.service.form;

import org.devgateway.toolkit.persistence.dao.form.AwardAcceptance;
import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisition;

/**
 * @author gmutuhu
 */
public interface AwardAcceptanceService extends AbstractMakueniEntityService<AwardAcceptance> {
    AwardAcceptance findByPurchaseRequisition(PurchaseRequisition purchaseRequisition);
}
