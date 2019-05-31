package org.devgateway.toolkit.persistence.service.form;

import org.devgateway.toolkit.persistence.dao.form.AwardAcceptance;
import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisition;
import org.devgateway.toolkit.persistence.repository.form.AwardAcceptanceRepository;
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
public class AwardAcceptanceServiceImpl extends AbstractMakueniEntityServiceImpl<AwardAcceptance>
        implements AwardAcceptanceService {

    @Autowired
    private AwardAcceptanceRepository awardAcceptanceRepository;

    @Override
    protected BaseJpaRepository<AwardAcceptance, Long> repository() {
        return awardAcceptanceRepository;
    }

    @Override
    public AwardAcceptance newInstance() {
        return new AwardAcceptance();
    }

    @Override
    // @Cacheable
    public AwardAcceptance findByPurchaseRequisition(final PurchaseRequisition purchaseRequisition) {
        return awardAcceptanceRepository.findByPurchaseRequisition(purchaseRequisition);
    }

}
