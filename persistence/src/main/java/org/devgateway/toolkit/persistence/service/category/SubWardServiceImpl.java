package org.devgateway.toolkit.persistence.service.category;

import org.devgateway.toolkit.persistence.dao.categories.SubWard;
import org.devgateway.toolkit.persistence.repository.category.SubWardRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author mpostelnicu
 */
@Service
@Transactional
public class SubWardServiceImpl extends CategoryServiceImpl<SubWard> implements SubWardService {
    @Autowired
    private SubWardRepository repository;

    @Override
    protected BaseJpaRepository<SubWard, Long> repository() {
        return repository;
    }

    @Override
    public SubWard newInstance() {
        return new SubWard();
    }
}
