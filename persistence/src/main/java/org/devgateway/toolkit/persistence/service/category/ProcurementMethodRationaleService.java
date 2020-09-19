package org.devgateway.toolkit.persistence.service.category;

import org.devgateway.toolkit.persistence.dao.categories.ProcurementMethodRationale;
import org.devgateway.toolkit.persistence.service.BaseJpaService;
import org.devgateway.toolkit.persistence.service.TextSearchableService;

public interface ProcurementMethodRationaleService extends BaseJpaService<ProcurementMethodRationale>,
        TextSearchableService<ProcurementMethodRationale> {

}
