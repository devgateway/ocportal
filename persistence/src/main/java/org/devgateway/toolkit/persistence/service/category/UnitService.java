package org.devgateway.toolkit.persistence.service.category;

import org.devgateway.toolkit.persistence.dao.categories.Unit;
import org.devgateway.toolkit.persistence.service.BaseJpaService;
import org.devgateway.toolkit.persistence.service.TextSearchableService;

/**
 * @author idobre
 * @since 26/08/2019
 */
public interface UnitService extends BaseJpaService<Unit>, TextSearchableService<Unit> {

}
