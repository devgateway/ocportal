package org.devgateway.toolkit.persistence.repository.form;

import org.devgateway.toolkit.persistence.dao.form.ProfessionalOpinion;
import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisition;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author idobre
 * @since 2019-04-24
 */
@Transactional
public interface ProfessionalOpinionRepository extends AbstractMakueniEntityRepository<ProfessionalOpinion> {

    @Query("select professionalOpinion from  #{#entityName} professionalOpinion "
            + " where professionalOpinion.tenderQuotationEvaluation.tender.purchaseRequisition = :purchaseRequisition")
    List<ProfessionalOpinion> findByPurchaseRequisition(
            @Param("purchaseRequisition") PurchaseRequisition purchaseRequisition);

}
