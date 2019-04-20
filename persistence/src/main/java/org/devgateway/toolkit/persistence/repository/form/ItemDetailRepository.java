package org.devgateway.toolkit.persistence.repository.form;

import org.devgateway.toolkit.persistence.dao.form.ItemDetail;
import org.devgateway.toolkit.persistence.repository.norepository.TextSearchableRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author gmutuhu
 *
 */
@Transactional
public interface ItemDetailRepository extends TextSearchableRepository<ItemDetail, Long> {
    @Override
    @Query("select itemDetail from  #{#entityName} itemDetail where "
            + " lower(itemDetail.planItem.description) like %:description%")
    Page<ItemDetail> searchText(@Param("description") String description, Pageable page);
}
