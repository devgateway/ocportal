package org.devgateway.toolkit.persistence.service.form;

import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.dao.form.Project;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess;
import org.devgateway.toolkit.persistence.service.TextSearchableService;

import java.util.List;

/**
 * @author idobre
 * @since 2019-04-17
 */
public interface TenderProcessService extends AbstractMakueniEntityService<TenderProcess>,
        TextSearchableService<TenderProcess> {
    List<TenderProcess> findByProject(Project project);

    List<TenderProcess> findByProjectProcurementPlan(ProcurementPlan procurementPlan);
}

