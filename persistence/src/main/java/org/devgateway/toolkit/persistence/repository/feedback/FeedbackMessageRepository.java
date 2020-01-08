package org.devgateway.toolkit.persistence.repository.feedback;

import org.devgateway.toolkit.persistence.dao.feedback.FeedbackMessage;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author mpostelnicu
 */
@Transactional
public interface FeedbackMessageRepository extends BaseJpaRepository<FeedbackMessage, Long> {

    List<FeedbackMessage> findByUrl(String url);
}
