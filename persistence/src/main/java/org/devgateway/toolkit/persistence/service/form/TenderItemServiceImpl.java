package org.devgateway.toolkit.persistence.service.form;

import org.devgateway.toolkit.persistence.dao.form.PurchaseItem;
import org.devgateway.toolkit.persistence.dao.form.TenderItem;
import org.devgateway.toolkit.persistence.repository.form.TenderItemRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.devgateway.toolkit.persistence.service.BaseJpaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author idobre
 * @since 02/09/2019
 */
@Service
@Transactional(readOnly = true)
public class TenderItemServiceImpl extends BaseJpaServiceImpl<TenderItem> implements TenderItemService {
    @Autowired
    private TenderItemRepository repository;

    @Override
    protected BaseJpaRepository<TenderItem, Long> repository() {
        return repository;
    }

    @Override
    public List<TenderItem> findByPurchaseItem(final PurchaseItem purchaseItem) {
        return repository.findByPurchaseItem(purchaseItem);
    }

    @Override
    public TenderItem newInstance() {
        return new TenderItem();
    }
}
