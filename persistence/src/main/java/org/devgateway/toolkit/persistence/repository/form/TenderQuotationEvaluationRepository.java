package org.devgateway.toolkit.persistence.repository.form;

import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisition;
import org.devgateway.toolkit.persistence.dao.form.TenderQuotationEvaluation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author gmutuhu
 */
@Transactional
public interface TenderQuotationEvaluationRepository
        extends AbstractMakueniEntityRepository<TenderQuotationEvaluation> {
    @Query("select tenderEvaluation from  #{#entityName} tenderEvaluation "
            + " where lower(tenderEvaluation.tender.tenderTitle) like %:name%")
    Page<TenderQuotationEvaluation> searchText(@Param("name") String name, Pageable page);

    @Query("select tenderEvaluation from  #{#entityName} tenderEvaluation "
            + " where tenderEvaluation.tender.purchaseRequisition = :purchaseRequisition")
    List<TenderQuotationEvaluation> findByPurchaseRequisition(
            @Param("purchaseRequisition") PurchaseRequisition purchaseRequisition);
}
