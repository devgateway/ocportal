package org.devgateway.toolkit.persistence.service.feedback;

import org.devgateway.toolkit.persistence.dao.feedback.ReplyableFeedbackMessage;

import java.util.List;

/**
 * @author mpostelnicu
 */
public interface ReplyableFeedbackMessageService extends GenericFeedbackMessageService<ReplyableFeedbackMessage> {

    List<ReplyableFeedbackMessage> findByUrl(String url);

    List<ReplyableFeedbackMessage> findByUrlAndVisibleTrue(String url);
}