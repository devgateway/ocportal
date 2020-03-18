package org.devgateway.toolkit.persistence.service.sms;

import org.devgateway.toolkit.persistence.dao.sms.SMSMessage;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.devgateway.toolkit.persistence.repository.sms.SMSMessageRepository;
import org.devgateway.toolkit.persistence.service.BaseJpaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author mpostelnicu
 */
@Service
@Transactional
public class SMSMessageServiceImpl extends BaseJpaServiceImpl<SMSMessage> implements SMSMessageService {
    @Autowired
    private SMSMessageRepository repository;

    @Override
    protected BaseJpaRepository<SMSMessage, Long> repository() {
        return repository;
    }

    @Override
    public SMSMessage newInstance() {
        return new SMSMessage();
    }
}

