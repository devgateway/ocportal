package org.devgateway.toolkit.persistence.service.form;

import org.devgateway.toolkit.persistence.dao.form.AwardNotification;
import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisition;

/**
 * @author gmutuhu
 */
public interface AwardNotificationService extends AbstractMakueniEntityService<AwardNotification> {
    AwardNotification findByPurchaseRequisition(PurchaseRequisition purchaseRequisition);
}
