package org.devgateway.toolkit.persistence.service.form;

import org.devgateway.toolkit.persistence.dao.form.Contract;
import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisition;
import org.devgateway.toolkit.persistence.service.BaseJpaService;

/**
 * @author gmutuhu
 */
public interface ContractService extends BaseJpaService<Contract> {
    Contract findByPurchaseRequisition(PurchaseRequisition purchaseRequisition);
}
