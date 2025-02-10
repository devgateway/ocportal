package org.devgateway.toolkit.persistence.repository.form;

import org.devgateway.toolkit.persistence.dao.form.PaymentVoucher;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface PaymentVoucherRepository extends AbstractImplTenderProcessClientEntityRepository<PaymentVoucher> {
}
