package org.devgateway.toolkit.persistence.repository.form;

import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.dao.form.FiscalYearBudget;
import org.devgateway.toolkit.persistence.repository.norepository.TextSearchableRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author mpostelnicu
 */
@Transactional
public interface FiscalYearBudgetRepository extends TextSearchableRepository<FiscalYearBudget, Long> {

    @Query("select c from  #{#entityName} c where c.fiscalYear = :fiscalYear")
    List<FiscalYearBudget> findByFiscalYear(@Param("fiscalYear") FiscalYear fiscalYear);
}
