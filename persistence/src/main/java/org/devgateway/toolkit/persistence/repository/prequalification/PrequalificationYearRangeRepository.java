package org.devgateway.toolkit.persistence.repository.prequalification;

import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationYearRange;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface PrequalificationYearRangeRepository extends BaseJpaRepository<PrequalificationYearRange, Long> {
    @Query("select count(r) from PrequalificationYearRange r where r.id <> :id and r.name = :name")
    long countByName(@Param("id") Long id, @Param("name") String name);
}
