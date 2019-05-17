package org.devgateway.toolkit.persistence.repository.form;

import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.dao.form.Project;
import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author idobre
 * @since 2019-04-17
 */
@Transactional
public interface PurchaseRequisitionRepository extends AbstractMakueniEntityRepository<PurchaseRequisition> {
    @Override
    @Query("select purchase from #{#entityName} purchase where lower(purchase.title) like %:name%")
    Page<PurchaseRequisition> searchText(@Param("name") String name, Pageable page);

    Long countByProjectProcurementPlanAndTitleAndIdNot(ProcurementPlan procurementPlan, String title, Long id);
    
    List<PurchaseRequisition> findByProject(Project project);

    @Query("select c from  #{#entityName} c "
            + " where c.project.procurementPlan.fiscalYear.id = :fiscalYearId")
    List<PurchaseRequisition> findByFiscalYearId(@Param("fiscalYearId") Long  id);
}
