package org.devgateway.toolkit.persistence.service.category;

import org.devgateway.toolkit.persistence.dao.categories.ProcuringEntity;
import org.devgateway.toolkit.persistence.service.BaseJpaService;
import org.devgateway.toolkit.persistence.service.TextSearchableService;

/**
 * @author gmutuhu
 *
 */
public interface ProcuringEntityService
        extends BaseJpaService<ProcuringEntity>, TextSearchableService<ProcuringEntity> {

}
