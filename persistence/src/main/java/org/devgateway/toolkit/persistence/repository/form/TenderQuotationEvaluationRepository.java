package org.devgateway.toolkit.persistence.repository.form;

import org.devgateway.toolkit.persistence.dao.form.TenderQuotationEvaluation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author gmutuhu
 */
@Transactional
public interface TenderQuotationEvaluationRepository
        extends AbstractPurchaseReqMakueniEntityRepository<TenderQuotationEvaluation> {
    @Query("select tenderEvaluation from  #{#entityName} tenderEvaluation "
            + " where lower(purchaseRequisition.tender.tenderTitle) like %:name%")
    Page<TenderQuotationEvaluation> searchText(@Param("name") String name, Pageable page);

}
