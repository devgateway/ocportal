package org.devgateway.toolkit.persistence.service.form;

import org.devgateway.toolkit.persistence.dao.form.AwardAcceptance;
import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisition;
import org.devgateway.toolkit.persistence.repository.form.AwardAcceptanceRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.devgateway.toolkit.persistence.service.BaseJpaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author gmutuhu
 */
@Service
@Transactional(readOnly = true)
public class AwardAcceptanceServiceImpl extends BaseJpaServiceImpl<AwardAcceptance> implements AwardAcceptanceService {

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
    public AwardAcceptance findByPurchaseRequisition(final PurchaseRequisition purchaseRequisition) {
        return awardAcceptanceRepository.findByPurchaseRequisition(purchaseRequisition).stream().findFirst()
                .orElse(null);
    }

}
