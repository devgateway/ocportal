package org.devgateway.toolkit.persistence.service.category;

import org.devgateway.toolkit.persistence.dao.categories.TargetGroup;
import org.devgateway.toolkit.persistence.repository.category.TargetGroupRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.devgateway.toolkit.persistence.repository.norepository.TextSearchableRepository;
import org.devgateway.toolkit.persistence.service.BaseJpaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author idobre
 * @since 2019-03-11
 */
@Service
@Transactional(readOnly = true)
public class TargetGroupServiceImpl extends BaseJpaServiceImpl<TargetGroup> implements TargetGroupService {
    @Autowired
    private TargetGroupRepository repository;

    @Override
    protected BaseJpaRepository<TargetGroup, Long> repository() {
        return repository;
    }

    @Override
    public TextSearchableRepository<TargetGroup, Long> textRepository() {
        return repository;
    }

    @Override
    public TargetGroup newInstance() {
        return new TargetGroup();
    }
}

