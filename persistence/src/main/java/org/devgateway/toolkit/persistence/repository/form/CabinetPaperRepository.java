package org.devgateway.toolkit.persistence.repository.form;

import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.dao.form.CabinetPaper;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author gmutuhu
 *
 */
@Transactional
public interface CabinetPaperRepository extends AbstractClientEntityRepository<CabinetPaper> {
    
    Long countByProcurementPlanAndNameAndIdNot(ProcurementPlan procurementPlan, String name, Long id);

    List<CabinetPaper> findByProcurementPlan(ProcurementPlan procurementPlan);

    @Override
    @Query("select c from  #{#entityName} c where c.procurementPlan.fiscalYear = :fiscalYear")
    List<CabinetPaper> findByFiscalYear(@Param("fiscalYear") FiscalYear fiscalYear);
}
