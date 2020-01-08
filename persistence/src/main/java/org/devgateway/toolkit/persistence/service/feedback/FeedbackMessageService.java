package org.devgateway.toolkit.persistence.service.feedback;

import org.devgateway.toolkit.persistence.dao.feedback.FeedbackMessage;
import org.devgateway.toolkit.persistence.service.BaseJpaService;

import java.util.List;

/**
 * @author mpostelnicu
 */
public interface FeedbackMessageService extends BaseJpaService<FeedbackMessage> {

    List<FeedbackMessage> findByUrl(String url);
}