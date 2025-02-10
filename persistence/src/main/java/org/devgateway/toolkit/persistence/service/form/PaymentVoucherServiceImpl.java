package org.devgateway.toolkit.persistence.service.form;


import org.devgateway.toolkit.persistence.dao.form.PaymentVoucher;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess;
import org.devgateway.toolkit.persistence.repository.form.PaymentVoucherRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author mpostelnicu
 */
@Service
@Transactional
public class PaymentVoucherServiceImpl extends AbstractImplTenderProcessClientEntityServiceImpl<PaymentVoucher>
        implements PaymentVoucherService {

    @Autowired
    private PaymentVoucherRepository repository;

    @Override
    protected BaseJpaRepository<PaymentVoucher, Long> repository() {
        return repository;
    }

    @Override
    public PaymentVoucher newInstance() {
        return new PaymentVoucher();
    }

    @Override
    public List<PaymentVoucher> findByTenderProcess(final TenderProcess tenderProcess) {
        return repository.findByTenderProcess(tenderProcess);
    }
}
