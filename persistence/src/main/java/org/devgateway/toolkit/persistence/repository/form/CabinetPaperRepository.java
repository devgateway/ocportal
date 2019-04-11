package org.devgateway.toolkit.persistence.repository.form;

import org.devgateway.toolkit.persistence.dao.form.CabinetPaper;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author gmutuhu
 *
 */
@Transactional
public interface CabinetPaperRepository extends BaseJpaRepository<CabinetPaper, Long> {

}
