package org.devgateway.toolkit.persistence.service.category;

import org.devgateway.toolkit.persistence.dao.categories.Subcounty;
import org.devgateway.toolkit.persistence.repository.category.SubcountyRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.devgateway.toolkit.persistence.repository.norepository.TextSearchableRepository;
import org.devgateway.toolkit.persistence.service.BaseJpaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author idobre
 * @since 2019-06-26
 */
@Service
@Transactional(readOnly = true)
public class SubcountyServiceImpl extends BaseJpaServiceImpl<Subcounty> implements SubcountyService {
    @Autowired
    private SubcountyRepository repository;

    @Override
    protected BaseJpaRepository<Subcounty, Long> repository() {
        return repository;
    }

    @Override
    public TextSearchableRepository<Subcounty, Long> textRepository() {
        return repository;
    }

    @Override
    public Subcounty newInstance() {
        return new Subcounty();
    }
}

