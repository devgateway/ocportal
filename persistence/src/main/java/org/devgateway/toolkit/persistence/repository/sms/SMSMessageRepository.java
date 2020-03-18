package org.devgateway.toolkit.persistence.repository.sms;

import org.devgateway.toolkit.persistence.dao.sms.SMSMessage;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;

public interface SMSMessageRepository extends BaseJpaRepository<SMSMessage, Long> {
}
