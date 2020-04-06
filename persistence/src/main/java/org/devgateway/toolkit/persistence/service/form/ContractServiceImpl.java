package org.devgateway.toolkit.persistence.service.form;

import org.devgateway.toolkit.persistence.dao.form.Contract;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess;
import org.devgateway.toolkit.persistence.repository.form.ContractRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author gmutuhu
 */
@Service
@Transactional(readOnly = true)
public class ContractServiceImpl extends AbstractMakueniEntityServiceImpl<Contract>
        implements ContractService {

    @Autowired
    private ContractRepository contractRepository;

    @Override
    protected BaseJpaRepository<Contract, Long> repository() {
        return contractRepository;
    }

    @Override
    public Contract newInstance() {
        return new Contract();
    }

    @Override
    @Cacheable
    public Contract findByTenderProcess(final TenderProcess tenderProcess) {
        return contractRepository.findByTenderProcess(tenderProcess);
    }

}
