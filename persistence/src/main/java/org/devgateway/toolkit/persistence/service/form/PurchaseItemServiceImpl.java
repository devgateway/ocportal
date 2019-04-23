package org.devgateway.toolkit.persistence.service.form;


import org.devgateway.toolkit.persistence.dao.form.PurchaseItem;
import org.devgateway.toolkit.persistence.repository.form.PurchaseItemRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.devgateway.toolkit.persistence.repository.norepository.TextSearchableRepository;
import org.devgateway.toolkit.persistence.service.BaseJpaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author gmutuhu
 *
 */
@Service
@Transactional(readOnly = true)
public class PurchaseItemServiceImpl extends BaseJpaServiceImpl<PurchaseItem> implements PurchaseItemService {

    @Autowired
    private PurchaseItemRepository purchaseItemRepository;

    @Override
    protected BaseJpaRepository<PurchaseItem, Long> repository() {
        return purchaseItemRepository;
    }

    @Override
    public PurchaseItem newInstance() {
        return new PurchaseItem();
    }

    @Override
    public TextSearchableRepository<PurchaseItem, Long> textRepository() {
        return purchaseItemRepository;
    }

}
