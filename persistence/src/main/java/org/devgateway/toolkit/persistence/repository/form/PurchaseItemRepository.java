package org.devgateway.toolkit.persistence.repository.form;

import org.devgateway.toolkit.persistence.dao.form.PlanItem;
import org.devgateway.toolkit.persistence.dao.form.PurchaseItem;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author gmutuhu
 */
@Transactional
public interface PurchaseItemRepository extends BaseJpaRepository<PurchaseItem, Long> {

    List<PurchaseItem> findByPlanItem(PlanItem planItem);
}
