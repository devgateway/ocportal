package org.devgateway.toolkit.persistence.repository.feedback;

import org.devgateway.toolkit.persistence.dao.feedback.FeedbackMessage;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @author mpostelnicu
 */
@NoRepositoryBean
public interface GenericFeedbackMessageRepository<T extends FeedbackMessage> extends BaseJpaRepository<T, Long> {
}
