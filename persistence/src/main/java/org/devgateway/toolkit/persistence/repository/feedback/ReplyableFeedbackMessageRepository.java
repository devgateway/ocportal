package org.devgateway.toolkit.persistence.repository.feedback;

import org.devgateway.toolkit.persistence.dao.feedback.ReplyableFeedbackMessage;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author mpostelnicu
 */
@Transactional
public interface ReplyableFeedbackMessageRepository extends GenericFeedbackMessageRepository<ReplyableFeedbackMessage> {

    List<ReplyableFeedbackMessage> findByUrl(String url);

    List<ReplyableFeedbackMessage> findByUrlAndVisibleTrue(String url);
}
