package org.devgateway.toolkit.persistence.repository.form;

import java.util.List;

import org.devgateway.toolkit.persistence.dao.form.AwardNotification;
import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisition;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author gmutuhu
 */
@Transactional
public interface AwardNotificationRepository extends BaseJpaRepository<AwardNotification, Long> {

    @Query("select awardNotification from  #{#entityName} awardNotification "
            + " where awardNotification.tenderQuotationEvaluation.tender.purchaseRequisition = :purchaseRequisition")
    List<AwardNotification> findByPurchaseRequisition(
            @Param("purchaseRequisition") PurchaseRequisition purchaseRequisition);

}
