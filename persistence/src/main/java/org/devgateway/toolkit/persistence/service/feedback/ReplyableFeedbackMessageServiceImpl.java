package org.devgateway.toolkit.persistence.service.feedback;

import org.devgateway.toolkit.persistence.dao.feedback.FeedbackMessage;
import org.devgateway.toolkit.persistence.dao.feedback.ReplyableFeedbackMessage;
import org.devgateway.toolkit.persistence.repository.feedback.ReplyableFeedbackMessageRepository;
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
public class ReplyableFeedbackMessageServiceImpl extends BaseJpaServiceImpl<ReplyableFeedbackMessage>
        implements ReplyableFeedbackMessageService {
    @Autowired
    private ReplyableFeedbackMessageRepository repository;

    @Override
    protected BaseJpaRepository<ReplyableFeedbackMessage, Long> repository() {
        return repository;
    }


    @Override
    public ReplyableFeedbackMessage newInstance() {
        return new ReplyableFeedbackMessage();
    }

    @Override
    public List<FeedbackMessage> findByUrl(String url) {
        return repository.findByUrl(url);
    }
}

