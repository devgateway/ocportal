package org.devgateway.toolkit.persistence.service.form;

import org.devgateway.toolkit.persistence.dao.form.PlanItem;
import org.devgateway.toolkit.persistence.repository.form.PlanItemRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.devgateway.toolkit.persistence.repository.norepository.TextSearchableRepository;
import org.devgateway.toolkit.persistence.service.BaseJpaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author idobre
 * @since 2019-04-18
 */
@Service
@Transactional(readOnly = true)
public class PlanItemServiceImpl extends BaseJpaServiceImpl<PlanItem> implements PlanItemService {

    @Autowired
    private PlanItemRepository planItemRepository;

    @Override
    protected BaseJpaRepository<PlanItem, Long> repository() {
        return planItemRepository;
    }

    @Override
    public TextSearchableRepository<PlanItem, Long> textRepository() {
        return planItemRepository;
    }

    @Override
    public PlanItem newInstance() {
        return new PlanItem();
    }
}
