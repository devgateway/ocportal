package org.devgateway.toolkit.persistence.service.prequalification;

import org.devgateway.toolkit.persistence.dao.prequalification.PrequalifiedSupplierItem;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.devgateway.toolkit.persistence.repository.prequalification.PrequalifiedSupplierItemRepository;
import org.devgateway.toolkit.persistence.service.BaseJpaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Octavian Ciubotaru
 */
@Service
public class PrequalifiedSupplierItemServiceImpl extends BaseJpaServiceImpl<PrequalifiedSupplierItem>
        implements PrequalifiedSupplierItemService {

    @Autowired
    private PrequalifiedSupplierItemRepository repository;

    @Override
    public PrequalifiedSupplierItem newInstance() {
        return new PrequalifiedSupplierItem();
    }

    @Override
    protected BaseJpaRepository<PrequalifiedSupplierItem, Long> repository() {
        return repository;
    }
}
