package org.devgateway.toolkit.persistence.repository.form;

import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.dao.form.Project;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author idobre
 * @since 2019-04-17
 */
@Transactional
public interface TenderProcessRepository extends AbstractMakueniEntityRepository<TenderProcess> {
    @Override
    @Query("select purchase from #{#entityName} purchase where lower(purchase.purchaseRequestNumber) like %:search%")
    Page<TenderProcess> searchText(@Param("search") String search, Pageable page);

    List<TenderProcess> findByProject(Project project);

    @EntityGraph(attributePaths = {"project", "tender", "tenderQuotationEvaluation", "professionalOpinion",
            "awardNotification", "awardAcceptance", "contract", "purchaseItems"})
    List<TenderProcess> findByProjectProcurementPlan(ProcurementPlan procurementPlan);

    @Override
    @Query("select c from  #{#entityName} c where c.project.procurementPlan.fiscalYear = :fiscalYear")
    List<TenderProcess> findByFiscalYear(@Param("fiscalYear") FiscalYear fiscalYear);
}

