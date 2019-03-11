package org.devgateway.toolkit.persistence.service.category;

import org.devgateway.toolkit.persistence.dao.categories.ProcurementMethod;
import org.devgateway.toolkit.persistence.repository.category.ProcurementMethodRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.devgateway.toolkit.persistence.repository.norepository.TextSearchableRepository;
import org.devgateway.toolkit.persistence.service.BaseJpaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author idobre
 * @since 2019-03-11
 */
public class ProcurementMethodServiceImpl extends BaseJpaServiceImpl<ProcurementMethod>
        implements ProcurementMethodService {
    @Autowired
    private ProcurementMethodRepository repository;

    @Override
    protected BaseJpaRepository<ProcurementMethod, Long> repository() {
        return repository;
    }

    @Override
    public TextSearchableRepository<ProcurementMethod, Long> textRepository() {
        return repository;
    }

    @Override
    public ProcurementMethod newInstance() {
        return new ProcurementMethod();
    }
}
