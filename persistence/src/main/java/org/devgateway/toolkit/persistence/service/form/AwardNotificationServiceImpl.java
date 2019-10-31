package org.devgateway.toolkit.persistence.service.form;

import org.devgateway.toolkit.persistence.dao.form.AwardNotification;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess;
import org.devgateway.toolkit.persistence.repository.form.AwardNotificationRepository;
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
public class AwardNotificationServiceImpl extends AbstractMakueniEntityServiceImpl<AwardNotification>
        implements AwardNotificationService {

    @Autowired
    private AwardNotificationRepository awardNotificationRepository;

    @Override
    protected BaseJpaRepository<AwardNotification, Long> repository() {
        return awardNotificationRepository;
    }

    @Override
    public AwardNotification newInstance() {
        return new AwardNotification();
    }

    @Override
    @Cacheable
    public AwardNotification findByTenderProcess(final TenderProcess tenderProcess) {
        return awardNotificationRepository.findByTenderProcess(tenderProcess);
    }

}
