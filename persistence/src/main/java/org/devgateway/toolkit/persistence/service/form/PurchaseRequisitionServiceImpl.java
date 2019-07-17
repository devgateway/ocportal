package org.devgateway.toolkit.persistence.service.form;

import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.dao.form.Project;
import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisition;
import org.devgateway.toolkit.persistence.repository.form.PurchaseRequisitionRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.devgateway.toolkit.persistence.repository.norepository.TextSearchableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.devgateway.toolkit.persistence.dao.DBConstants.Status.APPROVED;

/**
 * @author idobre
 * @since 2019-04-17
 */
@Service
@Transactional(readOnly = true)
public class PurchaseRequisitionServiceImpl extends AbstractMakueniEntityServiceImpl<PurchaseRequisition>
        implements PurchaseRequisitionService {
    @Autowired
    private PurchaseRequisitionRepository purchaseRequisitionRepository;

    @Override
    protected BaseJpaRepository<PurchaseRequisition, Long> repository() {
        return purchaseRequisitionRepository;
    }

    @Override
    public TextSearchableRepository<PurchaseRequisition, Long> textRepository() {
        return purchaseRequisitionRepository;
    }

    @Override
    public PurchaseRequisition newInstance() {
        return new PurchaseRequisition();
    }

    @Cacheable
    @Override
    public Long countByProjectProcurementPlanAndTitleAndIdNot(final ProcurementPlan procurementPlan,
                                               final String title,
                                               final Long id) {
        return purchaseRequisitionRepository.countByProjectProcurementPlanAndTitleAndIdNot(procurementPlan, title, id);
    }

    @Cacheable
    @Override
    public List<PurchaseRequisition> findByProject(final Project project) {
        return purchaseRequisitionRepository.findByProject(project);
    }

    @Cacheable
    @Override
    public List<PurchaseRequisition> findByProjectProcurementPlan(final ProcurementPlan procurementPlan) {
        return purchaseRequisitionRepository.findByProjectProcurementPlan(procurementPlan);
    }

    @Override
    public List<PurchaseRequisition> getApprovedPurchaseRequisitions() {
        return purchaseRequisitionRepository.findByStatus(APPROVED);
    }

}

