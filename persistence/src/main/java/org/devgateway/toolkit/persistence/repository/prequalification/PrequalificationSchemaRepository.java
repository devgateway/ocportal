package org.devgateway.toolkit.persistence.repository.prequalification;

import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationSchema;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface PrequalificationSchemaRepository extends BaseJpaRepository<PrequalificationSchema, Long> {


    @Query("select count(p) from PrequalificationSchema p "
            + "where p.id <> :id and (p.name = :name or p.prefix = :prefix)")
    long countByNameOrPrefix(@Param("id") Long id,
            @Param("name") String name, @Param("prefix") String prefix);
}
