package org.devgateway.toolkit.persistence.repository.form;

import org.devgateway.toolkit.persistence.dao.form.PurchaseItem;
import org.devgateway.toolkit.persistence.dao.form.TenderItem;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author idobre
 * @since 02/09/2019
 */
@Transactional
public interface TenderItemRepository extends BaseJpaRepository<TenderItem, Long> {
    List<TenderItem> findByPurchaseItem(PurchaseItem purchaseItem);
}
