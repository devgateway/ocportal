package org.devgateway.toolkit.persistence.repository.form;

import org.devgateway.toolkit.persistence.dao.form.AwardAcceptance;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author gmutuhu
*/
@Transactional
public interface AwardAcceptanceRepository extends BaseJpaRepository<AwardAcceptance, Long> {

}