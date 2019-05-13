package org.devgateway.toolkit.persistence.repository.form;

import java.util.List;

import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisition;
import org.devgateway.toolkit.persistence.dao.form.Tender;
import org.devgateway.toolkit.persistence.dao.form.TenderQuotationEvaluation;
import org.devgateway.toolkit.persistence.repository.norepository.TextSearchableRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author gmutuhu
 *
 */
@Transactional
public interface TenderQuotationEvaluationRepository extends TextSearchableRepository<TenderQuotationEvaluation, Long> {
    @Query("select tenderEvaluation from  #{#entityName} tenderEvaluation "
            + " where lower(tenderEvaluation.tender.tenderTitle) like %:name%")
    Page<TenderQuotationEvaluation> searchText(@Param("name") String name, Pageable page);

    @Query("select tenderEvaluation from  #{#entityName} tenderEvaluation "
            + " where tenderEvaluation.tender.purchaseRequisition = :purchaseRequisition")
    List<TenderQuotationEvaluation> findByPurchaseRequisition(
            @Param("purchaseRequisition") PurchaseRequisition purchaseRequisition);
}
