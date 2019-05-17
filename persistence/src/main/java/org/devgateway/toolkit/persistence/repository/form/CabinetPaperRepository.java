package org.devgateway.toolkit.persistence.repository.form;

import org.devgateway.toolkit.persistence.dao.form.CabinetPaper;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author gmutuhu
 *
 */
@Transactional
public interface CabinetPaperRepository extends AbstractMakueniEntityRepository<CabinetPaper> {
    @Override
    @Query("select cabinet from  #{#entityName} cabinet where lower(cabinet.name) like %:name%")
    Page<CabinetPaper> searchText(@Param("name") String name, Pageable page);
    
    Long countByProcurementPlanAndNameAndIdNot(ProcurementPlan procurementPlan, String name, Long id);

    @Query("select c from  #{#entityName} c "
            + " where c.procurementPlan.fiscalYear.id = :fiscalYearId")
    List<CabinetPaper> findByFiscalYearId(@Param("fiscalYearId") Long  id);
}
