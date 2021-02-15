package org.devgateway.toolkit.persistence.repository.prequalification;

import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationSchema;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationYearRange;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.devgateway.toolkit.persistence.repository.norepository.TextSearchableRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface PrequalificationSchemaRepository extends BaseJpaRepository<PrequalificationSchema, Long>,
        TextSearchableRepository<PrequalificationSchema, Long> {

    @Override
    @Query("select cat from  #{#entityName} cat where lower(cat.name) like %:name%")
    Page<PrequalificationSchema> searchText(@Param("name") String name, Pageable page);
}
