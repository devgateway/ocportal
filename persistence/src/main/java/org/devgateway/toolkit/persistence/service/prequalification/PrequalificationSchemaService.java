package org.devgateway.toolkit.persistence.service.prequalification;

import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationSchema;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationSchema_;
import org.devgateway.toolkit.persistence.service.BaseJpaService;
import org.devgateway.toolkit.persistence.service.TextSearchableService;
import org.springframework.data.jpa.domain.Specification;

public interface PrequalificationSchemaService extends BaseJpaService<PrequalificationSchema>,
        TextSearchableService<PrequalificationSchema> {

    long countByNameOrPrefix(PrequalificationSchema schema);

    Long saveDuplicateSchema(Long schemaId);

    static Specification<PrequalificationSchema> selectableSpecification() {
        return (r, cq, cb) ->
                cb.and(cb.equal(r.get(PrequalificationSchema_.status), DBConstants.Status.SUBMITTED));
    }
}
