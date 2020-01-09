package org.devgateway.toolkit.persistence.service.feedback;

import org.devgateway.toolkit.persistence.dao.feedback.FeedbackMessage;
import org.devgateway.toolkit.persistence.service.BaseJpaService;

/**
 * @author mpostelnicu
 */
public interface GenericFeedbackMessageService<T extends FeedbackMessage> extends BaseJpaService<T> {

}