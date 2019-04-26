package org.devgateway.toolkit.persistence.service.form;

import org.devgateway.toolkit.persistence.dao.form.CabinetPaper;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.service.BaseJpaService;
import org.devgateway.toolkit.persistence.service.TextSearchableService;

/**
 * @author gmutuhu
 *
 */
public interface CabinetPaperService extends BaseJpaService<CabinetPaper>, TextSearchableService<CabinetPaper> {
    Long countByProcurementPlanAndNameAndIdNot(ProcurementPlan procurementPlan, String name, Long id);
}
