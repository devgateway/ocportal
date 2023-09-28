package org.devgateway.toolkit.persistence.service.category;

import org.devgateway.toolkit.persistence.dao.categories.ProcurementMethod;
import org.devgateway.toolkit.persistence.repository.category.ProcurementMethodRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author idobre
 * @since 2019-03-11
 */
@Service
@Transactional(readOnly = true)
public class ProcurementMethodServiceImpl extends CategoryServiceImpl<ProcurementMethod>
        implements ProcurementMethodService {
    @Autowired
    private ProcurementMethodRepository repository;

    @Override
    protected BaseJpaRepository<ProcurementMethod, Long> repository() {
        return repository;
    }

    @Override
    public ProcurementMethod newInstance() {
        return new ProcurementMethod();
    }

}
