package org.devgateway.toolkit.persistence.service.form;

import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.dao.form.AbstractClientEntity;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.dao.form.Project;
import org.devgateway.toolkit.persistence.dao.form.Statusable;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess;
import org.devgateway.toolkit.persistence.service.BaseJpaService;
import org.devgateway.toolkit.persistence.service.TextSearchableService;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.stream.Stream;

/**
 * @author idobre
 * @since 2019-04-17
 */
public interface TenderProcessService extends BaseJpaService<TenderProcess> {
    List<TenderProcess> findByProject(Project project);

    List<TenderProcess> findByFiscalYear(FiscalYear fiscalYear);

    List<TenderProcess> findByProjectProcurementPlan(ProcurementPlan procurementPlan);

    Stream<TenderProcess> findAllStream();

    BindingResult validate(TenderProcess tp);

    BindingResult validate(TenderProcess tp, AbstractClientEntity e);

    Long countByFiscalYear(FiscalYear fiscalYear);

    Long countByDepartmentAndFiscalYear(Department department, FiscalYear fiscalYear);

    /**
     * Return first visible downstream form class starting and including the passed form class.
     */
    Class<?> getFirstVisibleDownstreamForm(Class<?> formClass);

    /**
     * Returns first visible upstream entity in the {@link TenderProcess}
     * starting from and excluding the entity of the given class
     * @param tp
     * @param currentClazz
     * @return
     */
    Statusable getPreviousStatusable(TenderProcess tp, Class<?> currentClazz);

    AbstractClientEntity getNextStatusable(TenderProcess tp, Class<?> currentClazz);
}

