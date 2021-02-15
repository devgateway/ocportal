package org.devgateway.toolkit.persistence.service.prequalification;

import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationSchema;
import org.devgateway.toolkit.persistence.service.BaseJpaService;
import org.devgateway.toolkit.persistence.service.TextSearchableService;

public interface PrequalificationSchemaService extends BaseJpaService<PrequalificationSchema>,
        TextSearchableService<PrequalificationSchema> {
}
