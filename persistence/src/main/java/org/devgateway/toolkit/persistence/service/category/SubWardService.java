package org.devgateway.toolkit.persistence.service.category;

import org.devgateway.toolkit.persistence.dao.categories.SubWard;
import org.devgateway.toolkit.persistence.service.BaseJpaService;
import org.devgateway.toolkit.persistence.service.TextSearchableService;

/**
 * @author mpostelnicu
 */
public interface SubWardService extends BaseJpaService<SubWard>, TextSearchableService<SubWard> {

}

