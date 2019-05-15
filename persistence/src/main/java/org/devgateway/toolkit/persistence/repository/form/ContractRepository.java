package org.devgateway.toolkit.persistence.repository.form;

import org.devgateway.toolkit.persistence.dao.form.Contract;
import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisition;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author gmutuhu
*/
@Transactional
public interface ContractRepository extends AbstractMakueniEntityRepository<Contract> {

    @Query("select contract from  #{#entityName} contract "
            + " where contract.tenderQuotationEvaluation.tender.purchaseRequisition = :purchaseRequisition")
    List<Contract> findByPurchaseRequisition(@Param("purchaseRequisition") PurchaseRequisition purchaseRequisition);
}