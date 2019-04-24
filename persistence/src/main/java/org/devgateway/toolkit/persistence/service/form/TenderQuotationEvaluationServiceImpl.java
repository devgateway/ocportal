package org.devgateway.toolkit.persistence.service.form;

import org.devgateway.toolkit.persistence.dao.form.TenderQuotationEvaluation;
import org.devgateway.toolkit.persistence.repository.form.TenderQuotationEvaluationRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
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
public class TenderQuotationEvaluationServiceImpl extends BaseJpaServiceImpl<TenderQuotationEvaluation>
        implements TenderQuotationEvalutionService {

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

}
