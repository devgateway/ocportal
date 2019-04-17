package org.devgateway.toolkit.persistence.service.form;

import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisition;
import org.devgateway.toolkit.persistence.repository.form.PurchaseRequisitionRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.devgateway.toolkit.persistence.service.BaseJpaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author idobre
 * @since 2019-04-17
 */
@Service
@Transactional(readOnly = true)
public class PurchaseRequisitionServiceImpl extends BaseJpaServiceImpl<PurchaseRequisition>
        implements PurchaseRequisitionService {
    @Autowired
    private PurchaseRequisitionRepository purchaseRequisitionRepository;

    @Override
    protected BaseJpaRepository<PurchaseRequisition, Long> repository() {
        return purchaseRequisitionRepository;
    }

    @Override
    public PurchaseRequisition newInstance() {
        return new PurchaseRequisition();
    }
}

