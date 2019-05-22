package org.devgateway.toolkit.persistence.service.form;

import org.devgateway.toolkit.persistence.dao.form.CabinetPaper;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.service.TextSearchableService;

import java.util.List;

/**
 * @author gmutuhu
 *
 */
public interface CabinetPaperService extends AbstractMakueniEntityService<CabinetPaper>,
        TextSearchableService<CabinetPaper> {
    Long countByProcurementPlanAndNameAndIdNot(ProcurementPlan procurementPlan, String name, Long id);

    List<CabinetPaper> findByProcurementPlan(ProcurementPlan procurementPlan);
}
