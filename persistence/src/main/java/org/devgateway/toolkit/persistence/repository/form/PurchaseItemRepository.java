package org.devgateway.toolkit.persistence.repository.form;

import org.devgateway.toolkit.persistence.dao.form.PurchaseItem;
import org.devgateway.toolkit.persistence.repository.norepository.TextSearchableRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author gmutuhu
 *
 */
@Transactional
public interface PurchaseItemRepository extends TextSearchableRepository<PurchaseItem, Long> {
    @Override
    @Query("select purchaseItem from  #{#entityName} purchaseItem where "
            + " lower(purchaseItem.planItem.description) like %:description%")
    Page<PurchaseItem> searchText(@Param("description") String description, Pageable page);
}
