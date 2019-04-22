package org.devgateway.toolkit.persistence.repository.form;

import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisition;
import org.devgateway.toolkit.persistence.repository.norepository.TextSearchableRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author idobre
 * @since 2019-04-17
 */
@Transactional
public interface PurchaseRequisitionRepository extends TextSearchableRepository<PurchaseRequisition, Long> {
    @Override
    @Query("select purchase from #{#entityName} purchase where lower(purchase.title) like %:name%")
    Page<PurchaseRequisition> searchText(@Param("name") String name, Pageable page);

    Long countByProcurementPlanAndTitleAndIdNot(ProcurementPlan procurementPlan, String title, Long id);
}
