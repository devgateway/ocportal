package org.devgateway.toolkit.persistence.service.form;

import org.devgateway.toolkit.persistence.dao.categories.Item_;
import org.devgateway.toolkit.persistence.dao.form.PlanItem;
import org.devgateway.toolkit.persistence.dao.form.PlanItem_;
import org.devgateway.toolkit.persistence.repository.form.PlanItemRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.devgateway.toolkit.persistence.service.BaseJpaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.metamodel.SingularAttribute;

/**
 * @author idobre
 * @since 2019-04-18
 */
@Service
@Transactional
public class PlanItemServiceImpl extends BaseJpaServiceImpl<PlanItem> implements PlanItemService {

    @Autowired
    private PlanItemRepository planItemRepository;

    @Override
    protected BaseJpaRepository<PlanItem, Long> repository() {
        return planItemRepository;
    }

    @Override
    public PlanItem newInstance() {
        return new PlanItem();
    }

    @Override
    public SingularAttribute<? super PlanItem, String> getTextAttribute() {
       throw new UnsupportedOperationException();
    }

    @Override
    public Specification<PlanItem> getTextSpecification(String text) {
        return (root, query, cb) -> cb.like(cb.lower(root.join(PlanItem_.item)
                .get(Item_.label)), "%" + text.toLowerCase() + "%");
    }
}
