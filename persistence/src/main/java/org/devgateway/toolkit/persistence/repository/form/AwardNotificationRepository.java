package org.devgateway.toolkit.persistence.repository.form;

import org.devgateway.toolkit.persistence.dao.form.AwardNotification;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author gmutuhu
 */
@Transactional
public interface AwardNotificationRepository extends BaseJpaRepository<AwardNotification, Long> {

}
