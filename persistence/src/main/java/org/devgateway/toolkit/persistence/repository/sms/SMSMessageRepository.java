package org.devgateway.toolkit.persistence.repository.sms;

import org.devgateway.toolkit.persistence.dao.sms.SMSMessage;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

@Transactional
public interface SMSMessageRepository extends BaseJpaRepository<SMSMessage, Long> {

    Stream<SMSMessage> findByProcessedIsFalse();

}
