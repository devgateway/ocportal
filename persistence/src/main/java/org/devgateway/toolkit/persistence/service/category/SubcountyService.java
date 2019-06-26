package org.devgateway.toolkit.persistence.service.category;

import org.devgateway.toolkit.persistence.dao.categories.Subcounty;
import org.devgateway.toolkit.persistence.service.BaseJpaService;
import org.devgateway.toolkit.persistence.service.TextSearchableService;

/**
 * @author idobre
 * @since 2019-06-26
 */
public interface SubcountyService extends BaseJpaService<Subcounty>, TextSearchableService<Subcounty> {

}

