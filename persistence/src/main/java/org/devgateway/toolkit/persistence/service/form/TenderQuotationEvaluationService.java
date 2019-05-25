package org.devgateway.toolkit.persistence.service.form;

import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisition;
import org.devgateway.toolkit.persistence.dao.form.TenderQuotationEvaluation;
import org.devgateway.toolkit.persistence.service.TextSearchableService;

/**
 * @author gmutuhu
 *
 */
public interface TenderQuotationEvaluationService extends AbstractMakueniEntityService<TenderQuotationEvaluation>,
        TextSearchableService<TenderQuotationEvaluation> {
    TenderQuotationEvaluation findByPurchaseRequisition(PurchaseRequisition purchaseRequisition);
}
