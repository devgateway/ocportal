package org.devgateway.toolkit.persistence.repository.form;

import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

/**
 * @author idobre
 * @since 2019-04-02
 */
@Transactional
public interface ProcurementPlanRepository extends AbstractClientEntityRepository<ProcurementPlan> {

    Long countByDepartmentAndFiscalYear(Department department, FiscalYear fiscalYear);

    @Query("select count(p) from ProcurementPlan p "
            + "where p.department = :department "
            + "and p.fiscalYear = :fiscalYear "
            + "and p.id <> :id")
    Long countByDepartmentAndFiscalYearExceptId(
            @Param("department") Department department,
            @Param("fiscalYear") FiscalYear fiscalYear,
            @Param("id") Long exceptId);

    @Query("SELECT COUNT(p) FROM ProcurementPlan p "
            + "WHERE p.department.id = :departmentId "
            + "AND p.fiscalYear.id = :fiscalYearId "
            + "AND p.id <> :id")
    Long countByDepartmentAndFiscalYearExceptId(
            @Param("departmentId") Long departmentId,
            @Param("fiscalYearId") Long fiscalYearId,
            @Param("id") Long exceptId);

    ProcurementPlan findByDepartmentAndFiscalYear(Department department, FiscalYear fiscalYear);

    @Query("select p from ProcurementPlan p")
    Stream<ProcurementPlan> findAllStream();
}
