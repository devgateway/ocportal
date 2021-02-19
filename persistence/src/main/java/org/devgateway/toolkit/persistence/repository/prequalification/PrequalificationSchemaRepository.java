package org.devgateway.toolkit.persistence.repository.prequalification;

import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationSchema;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface PrequalificationSchemaRepository extends BaseJpaRepository<PrequalificationSchema, Long> {

}
