/**
 *
 */
package org.devgateway.toolkit.persistence.service.category;

import org.devgateway.toolkit.persistence.dao.categories.PMCStatus;
import org.devgateway.toolkit.persistence.repository.category.PMCStatusRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.devgateway.toolkit.persistence.repository.norepository.TextSearchableRepository;
import org.devgateway.toolkit.persistence.service.BaseJpaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author mpostelnicu
 *
 */
@Service
@Transactional
public class PMCStatusServiceImpl extends BaseJpaServiceImpl<PMCStatus> implements PMCStatusService {

    @Autowired
    private PMCStatusRepository repository;

    @Override
    protected BaseJpaRepository<PMCStatus, Long> repository() {
        return repository;
    }

    @Override
    public TextSearchableRepository<PMCStatus, Long> textRepository() {
        return repository;
    }

    @Override
    public PMCStatus newInstance() {
        return new PMCStatus();
    }
}
