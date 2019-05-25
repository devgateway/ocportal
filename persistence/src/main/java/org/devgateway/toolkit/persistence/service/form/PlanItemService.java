package org.devgateway.toolkit.persistence.service.form;

import org.devgateway.toolkit.persistence.dao.form.PlanItem;
import org.devgateway.toolkit.persistence.service.BaseJpaService;
import org.devgateway.toolkit.persistence.service.TextSearchableService;

/**
 * @author idobre
 * @since 2019-04-18
 */
public interface PlanItemService extends BaseJpaService<PlanItem>, TextSearchableService<PlanItem> {

}
