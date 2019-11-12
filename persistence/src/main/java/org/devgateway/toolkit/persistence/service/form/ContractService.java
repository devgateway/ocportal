package org.devgateway.toolkit.persistence.service.form;

import org.devgateway.toolkit.persistence.dao.form.Contract;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess;

/**
 * @author gmutuhu
 */
public interface ContractService extends AbstractMakueniEntityService<Contract> {
    Contract findByTenderProcess(TenderProcess tenderProcess);
}
