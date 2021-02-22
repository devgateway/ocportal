package org.devgateway.toolkit.persistence.service.prequalification;

import org.devgateway.toolkit.persistence.dao.categories.Supplier;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationYearRange;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalifiedSupplier;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.devgateway.toolkit.persistence.repository.prequalification.PrequalifiedSupplierRepository;
import org.devgateway.toolkit.persistence.service.BaseJpaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Octavian Ciubotaru
 */
@Service
public class PrequalifiedSupplierServiceImpl
        extends BaseJpaServiceImpl<PrequalifiedSupplier>
        implements PrequalifiedSupplierService {

    @Autowired
    private PrequalifiedSupplierRepository repository;

    @Override
    public PrequalifiedSupplier newInstance() {
        return new PrequalifiedSupplier();
    }

    @Override
    protected BaseJpaRepository<PrequalifiedSupplier, Long> repository() {
        return repository;
    }

    @Override
    public boolean isSupplierPrequalified(Supplier supplier, PrequalificationYearRange yearRange, Long exceptId) {
        return repository.countPrequalifiedSuppliers(supplier, yearRange, exceptId) > 0;
    }
}
