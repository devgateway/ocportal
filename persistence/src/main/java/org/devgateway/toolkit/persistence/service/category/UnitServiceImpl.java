package org.devgateway.toolkit.persistence.service.category;

import org.devgateway.toolkit.persistence.dao.categories.Unit;
import org.devgateway.toolkit.persistence.repository.category.UnitRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author idobre
 * @since 26/08/2019
 */
@Service
@Transactional(readOnly = true)
public class UnitServiceImpl extends CategoryServiceImpl<Unit> implements UnitService {
    @Autowired
    private UnitRepository repository;

    @Override
    protected BaseJpaRepository<Unit, Long> repository() {
        return repository;
    }

    @Override
    public Unit newInstance() {
        return new Unit();
    }

    @Override
    public Unit findByLabel(String label) {
        return repository.findByLabel(label);
    }

    @Override
    public Unit findByLabelIgnoreCase(String label) {
        return repository.findByLabelIgnoreCase(label);
    }
}

