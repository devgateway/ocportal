package org.devgateway.toolkit.persistence.repository.form;

import org.devgateway.toolkit.persistence.dao.form.PlanItem;
import org.devgateway.toolkit.persistence.dao.form.PurchaseItem;
import org.devgateway.toolkit.persistence.repository.norepository.TextSearchableRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author gmutuhu
 */
@Transactional
public interface PurchaseItemRepository extends TextSearchableRepository<PurchaseItem, Long> {
    @Override
    @Query("select purchaseItem from  #{#entityName} purchaseItem where "
            + " lower(purchaseItem.planItem.item.label) like %:search%")
    Page<PurchaseItem> searchText(@Param("search") String search, Pageable page);


    List<PurchaseItem> findByPlanItem(PlanItem planItem);
}
