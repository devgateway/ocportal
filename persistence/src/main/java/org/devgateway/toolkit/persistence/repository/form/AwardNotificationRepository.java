package org.devgateway.toolkit.persistence.repository.form;

import org.devgateway.toolkit.persistence.dao.form.AwardNotification;
import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisition;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author gmutuhu
 */
@Transactional
public interface AwardNotificationRepository extends AbstractMakueniEntityRepository<AwardNotification> {

    @Query("select awardNotification from  #{#entityName} awardNotification "
            + " where awardNotification.tenderQuotationEvaluation.tender.purchaseRequisition = :purchaseRequisition")
    List<AwardNotification> findByPurchaseRequisition(
            @Param("purchaseRequisition") PurchaseRequisition purchaseRequisition);

}
