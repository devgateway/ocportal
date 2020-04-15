package org.devgateway.toolkit.persistence.service.category;

import org.devgateway.toolkit.persistence.dao.categories.ProcurementMethodRationale;
import org.devgateway.toolkit.persistence.repository.category.ProcurementMethodRationaleRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.devgateway.toolkit.persistence.repository.norepository.TextSearchableRepository;
import org.devgateway.toolkit.persistence.service.BaseJpaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author mpostelnicu
 */
@Service
@Transactional
public class ProcurementMethodRationaleServiceImpl extends BaseJpaServiceImpl<ProcurementMethodRationale>
        implements ProcurementMethodRationaleService {
    @Autowired
    private ProcurementMethodRationaleRepository repository;

    @Override
    protected BaseJpaRepository<ProcurementMethodRationale, Long> repository() {
        return repository;
    }

    @Override
    public TextSearchableRepository<ProcurementMethodRationale, Long> textRepository() {
        return repository;
    }

    @Override
    public ProcurementMethodRationale newInstance() {
        return new ProcurementMethodRationale();
    }
}
