package org.devgateway.toolkit.persistence.service.form;

import org.devgateway.toolkit.persistence.dao.form.PurchaseItem;
import org.devgateway.toolkit.persistence.dao.form.TenderItem;
import org.devgateway.toolkit.persistence.service.BaseJpaService;

import java.util.List;

/**
 * @author idobre
 * @since 02/09/2019
 */
public interface TenderItemService extends BaseJpaService<TenderItem> {
    List<TenderItem> findByPurchaseItem(PurchaseItem purchaseItem);
}
