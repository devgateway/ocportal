package org.devgateway.toolkit.persistence.service.category;

import org.devgateway.toolkit.persistence.dao.categories.SupplierResponse;
import org.devgateway.toolkit.persistence.service.BaseJpaService;
import org.devgateway.toolkit.persistence.service.TextSearchableService;

/**
 * @author mpostelnicu
 */
public interface SupplierResponseService extends BaseJpaService<SupplierResponse>,
        TextSearchableService<SupplierResponse> {

}
