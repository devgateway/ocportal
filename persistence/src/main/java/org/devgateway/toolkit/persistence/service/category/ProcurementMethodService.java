package org.devgateway.toolkit.persistence.service.category;

import org.devgateway.toolkit.persistence.dao.categories.ProcurementMethod;
import org.devgateway.toolkit.persistence.service.BaseJpaService;
import org.devgateway.toolkit.persistence.service.TextSearchableService;

/**
 * @author idobre
 * @since 2019-03-11
 */
public interface ProcurementMethodService extends BaseJpaService<ProcurementMethod>,
        TextSearchableService<ProcurementMethod> {
}
