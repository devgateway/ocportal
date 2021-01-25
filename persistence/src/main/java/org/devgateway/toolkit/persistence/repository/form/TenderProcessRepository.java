package org.devgateway.toolkit.persistence.repository.form;

import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.dao.form.Project;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.devgateway.toolkit.persistence.repository.norepository.TextSearchableRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

/**
 * @author idobre
 * @since 2019-04-17
 */
@Transactional
public interface TenderProcessRepository extends BaseJpaRepository<TenderProcess, Long> {

    List<TenderProcess> findByProject(Project project);

    @Transactional(readOnly = true)
    List<TenderProcess> findByProjectProcurementPlan(ProcurementPlan procurementPlan);

    @Query("select c from  #{#entityName} c where c.procurementPlan.fiscalYear = :fiscalYear")
    List<TenderProcess> findByFiscalYear(@Param("fiscalYear") FiscalYear fiscalYear);


    @Query("select count(c) from  #{#entityName} c where c.procurementPlan.department = :department and "
            + "c.procurementPlan.fiscalYear = :fiscalYear")
    Long countByDepartmentAndFiscalYear(@Param("department") Department department,
                                        @Param("fiscalYear") FiscalYear fiscalYear);


    @Query("select count(c) from  #{#entityName} c where c.procurementPlan.fiscalYear = :fiscalYear")
    Long countByFiscalYear(@Param("fiscalYear") FiscalYear fiscalYear);

    @Query("select p from TenderProcess p")
    Stream<TenderProcess> findAllStream();
}

