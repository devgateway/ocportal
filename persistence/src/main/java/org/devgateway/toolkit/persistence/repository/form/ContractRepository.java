package org.devgateway.toolkit.persistence.repository.form;

import org.devgateway.toolkit.persistence.dao.form.Contract;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author gmutuhu
*/
@Transactional
public interface ContractRepository extends AbstractPurchaseReqMakueniEntityRepository<Contract> {

}