package org.devgateway.toolkit.persistence.service.category;

import org.devgateway.toolkit.persistence.dao.categories.Ward;
import org.devgateway.toolkit.persistence.repository.category.WardRepository;
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
public class WardServiceImpl extends BaseJpaServiceImpl<Ward> implements WardService {
    @Autowired
    private WardRepository repository;

    @Override
    protected BaseJpaRepository<Ward, Long> repository() {
        return repository;
    }

    @Override
    public TextSearchableRepository<Ward, Long> textRepository() {
        return repository;
    }

    @Override
    public Ward newInstance() {
        return new Ward();
    }
}
