package org.devgateway.toolkit.persistence.service.category;

import org.devgateway.toolkit.persistence.dao.categories.ProcuringEntity;
import org.devgateway.toolkit.persistence.repository.category.ProcuringEntityRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.devgateway.toolkit.persistence.repository.norepository.TextSearchableRepository;
import org.devgateway.toolkit.persistence.service.BaseJpaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author gmutuhu
 *
 */
@Service
@Transactional(readOnly = true)
public class ProcuringEntityServiceImpl extends BaseJpaServiceImpl<ProcuringEntity> implements ProcuringEntityService {

    @Autowired
    private ProcuringEntityRepository repository;

    @Override
    protected BaseJpaRepository<ProcuringEntity, Long> repository() {
        return repository;
    }

    @Override
    public TextSearchableRepository<ProcuringEntity, Long> textRepository() {
        return repository;
    }

    @Override
    public ProcuringEntity newInstance() {
        return new ProcuringEntity();
    }
}
