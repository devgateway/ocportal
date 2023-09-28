package org.devgateway.toolkit.persistence.repository.form;

import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.dao.form.FiscalYearBudget;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author mpostelnicu
 */
@Transactional
public interface FiscalYearBudgetRepository extends BaseJpaRepository<FiscalYearBudget, Long> {

    @Query("select c from  #{#entityName} c where c.fiscalYear = :fiscalYear")
    List<FiscalYearBudget> findByFiscalYear(@Param("fiscalYear") FiscalYear fiscalYear);

    FiscalYearBudget findByDepartmentAndFiscalYear(Department department, FiscalYear fiscalYear);

    Long countByDepartmentAndFiscalYear(Department department, FiscalYear fiscalYear);

    @Query("select count(b) "
            + "from FiscalYearBudget b "
            + "where b.department = :department "
            + "and b.fiscalYear = :fiscalYear "
            + "and b.id <> :id")
    Long countByDepartmentAndFiscalYearExcept(
            @Param("department") Department department,
            @Param("fiscalYear") FiscalYear fiscalYear,
            @Param("id") Long exceptId);
}
