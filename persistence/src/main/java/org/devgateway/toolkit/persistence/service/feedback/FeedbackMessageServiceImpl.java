package org.devgateway.toolkit.persistence.service.feedback;

import org.devgateway.toolkit.persistence.dao.feedback.FeedbackMessage;
import org.devgateway.toolkit.persistence.repository.feedback.FeedbackMessageRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.devgateway.toolkit.persistence.service.BaseJpaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author mpostelnicu
 */
@Service
@Transactional(readOnly = true)
public class FeedbackMessageServiceImpl extends BaseJpaServiceImpl<FeedbackMessage> implements FeedbackMessageService {
    @Autowired
    private FeedbackMessageRepository repository;

    @Override
    protected BaseJpaRepository<FeedbackMessage, Long> repository() {
        return repository;
    }


    @Override
    public FeedbackMessage newInstance() {
        return new FeedbackMessage();
    }

    @Override
    public List<FeedbackMessage> findByUrl(String url) {
        return repository.findByUrl(url);
    }
}

