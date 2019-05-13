package org.devgateway.toolkit.persistence.repository.form;

import java.util.List;

import org.devgateway.toolkit.persistence.dao.form.Contract;
import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisition;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author gmutuhu
*/
@Transactional
public interface ContractRepository extends BaseJpaRepository<Contract, Long> {

    @Query("select contract from  #{#entityName} contract "
            + " where contract.tenderQuotationEvaluation.tender.purchaseRequisition = :purchaseRequisition")
    List<Contract> findByPurchaseRequisition(@Param("purchaseRequisition") PurchaseRequisition purchaseRequisition);
}