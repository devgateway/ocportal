package org.devgateway.toolkit.persistence.repository.feedback;

import org.devgateway.toolkit.persistence.dao.feedback.FeedbackMessage;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author mpostelnicu
 */
@Transactional
public interface FeedbackMessageRepository extends GenericFeedbackMessageRepository<FeedbackMessage> {


}
