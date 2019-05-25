package org.devgateway.toolkit.persistence.repository.form;

import org.devgateway.toolkit.persistence.dao.form.PlanItem;
import org.devgateway.toolkit.persistence.repository.norepository.TextSearchableRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author idobre
 * @since 2019-04-18
 */
@Transactional
public interface PlanItemRepository extends TextSearchableRepository<PlanItem, Long> {
    @Override
    @Query("select planItem from  #{#entityName} planItem where lower(planItem.description) like %:description%")
    Page<PlanItem> searchText(@Param("description") String description, Pageable page);
}
