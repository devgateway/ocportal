package org.devgateway.toolkit.persistence.repository.form;

import java.util.List;

import org.devgateway.toolkit.persistence.dao.form.AwardAcceptance;
import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisition;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author gmutuhu
 */
@Transactional
public interface AwardAcceptanceRepository extends BaseJpaRepository<AwardAcceptance, Long> {

    @Query("select awardAcceptance from  #{#entityName} awardAcceptance "
            + " where awardAcceptance.tenderQuotationEvaluation.tender.purchaseRequisition = :purchaseRequisition")
    List<AwardAcceptance> findByPurchaseRequisition(
            @Param("purchaseRequisition") PurchaseRequisition purchaseRequisition);
}