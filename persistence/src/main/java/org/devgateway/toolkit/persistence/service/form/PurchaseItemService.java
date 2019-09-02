package org.devgateway.toolkit.persistence.service.form;

import org.devgateway.toolkit.persistence.dao.form.PlanItem;
import org.devgateway.toolkit.persistence.dao.form.PurchaseItem;
import org.devgateway.toolkit.persistence.service.BaseJpaService;
import org.devgateway.toolkit.persistence.service.TextSearchableService;

import java.util.List;

public interface PurchaseItemService extends BaseJpaService<PurchaseItem>, TextSearchableService<PurchaseItem> {
    List<PurchaseItem> findByPlanItem(PlanItem planItem);
}
