package org.devgateway.toolkit.persistence.service.form;

import org.devgateway.toolkit.persistence.dao.form.TenderProcess;
import org.devgateway.toolkit.persistence.dao.form.TenderQuotationEvaluation;
import org.devgateway.toolkit.persistence.repository.form.TenderQuotationEvaluationRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.devgateway.toolkit.persistence.repository.norepository.TextSearchableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author gmutuhu
 *
 */

@Service
@Transactional(readOnly = true)
public class TenderQuotationEvaluationServiceImpl extends AbstractMakueniEntityServiceImpl<TenderQuotationEvaluation>
        implements TenderQuotationEvaluationService {

    @Autowired
    private TenderQuotationEvaluationRepository repository;

    @Override
    protected BaseJpaRepository<TenderQuotationEvaluation, Long> repository() {
        return repository;
    }

    @Override
    public TenderQuotationEvaluation newInstance() {
        return new TenderQuotationEvaluation();
    }

    @Override
    public TextSearchableRepository<TenderQuotationEvaluation, Long> textRepository() {
        return repository;
    }

    @Override
    @Cacheable
    public TenderQuotationEvaluation findByTenderProcess(final TenderProcess tenderProcess) {
        return repository.findByTenderProcess(tenderProcess);
    }
}
