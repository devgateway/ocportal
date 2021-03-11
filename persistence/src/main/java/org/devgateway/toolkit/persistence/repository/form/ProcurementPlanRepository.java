package org.devgateway.toolkit.persistence.repository.form;

import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

/**
 * @author idobre
 * @since 2019-04-02
 */
@Transactional
public interface ProcurementPlanRepository extends AbstractMakueniEntityRepository<ProcurementPlan> {

    Long countByDepartmentAndFiscalYear(Department department, FiscalYear fiscalYear);

    ProcurementPlan findByDepartmentAndFiscalYear(Department department, FiscalYear fiscalYear);

    @Query("select p from ProcurementPlan p")
    Stream<ProcurementPlan> findAllStream();
}
