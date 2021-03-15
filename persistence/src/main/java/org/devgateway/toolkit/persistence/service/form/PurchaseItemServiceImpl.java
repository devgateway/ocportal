package org.devgateway.toolkit.persistence.service.form;


import org.devgateway.toolkit.persistence.dao.categories.Item_;
import org.devgateway.toolkit.persistence.dao.form.PlanItem;
import org.devgateway.toolkit.persistence.dao.form.PlanItem_;
import org.devgateway.toolkit.persistence.dao.form.PurchaseItem;
import org.devgateway.toolkit.persistence.dao.form.PurchaseItem_;
import org.devgateway.toolkit.persistence.repository.form.PurchaseItemRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.devgateway.toolkit.persistence.service.BaseJpaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.metamodel.SingularAttribute;
import java.util.List;

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

    public List<PurchaseItem> findByPlanItem(PlanItem planItem) {
        return purchaseItemRepository.findByPlanItem(planItem);
    }

    @Override
    public PurchaseItem newInstance() {
        return new PurchaseItem();
    }

    @Override
    public SingularAttribute<? super PurchaseItem, String> getTextAttribute() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Specification<PurchaseItem> getTextSpecification(String text) {
        return (root, query, cb) -> cb.like(cb.lower(root.join(PurchaseItem_.planItem)
                .join(PlanItem_.item).get(Item_.label)), "%" + text.toLowerCase() + "%");
    }
}
