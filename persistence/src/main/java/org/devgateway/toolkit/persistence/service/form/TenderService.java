package org.devgateway.toolkit.persistence.service.form;

import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisition;
import org.devgateway.toolkit.persistence.dao.form.Tender;
import org.devgateway.toolkit.persistence.service.TextSearchableService;

/**
 * @author gmutuhu
 *
 */
public interface TenderService extends AbstractMakueniEntityService<Tender>, TextSearchableService<Tender> {
    Tender findByPurchaseRequisition(PurchaseRequisition purchaseRequisition);
}
