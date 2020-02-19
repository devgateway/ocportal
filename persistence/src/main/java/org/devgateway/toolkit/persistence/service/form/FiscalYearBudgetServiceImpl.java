package org.devgateway.toolkit.persistence.service.form;

import org.devgateway.toolkit.persistence.dao.form.FiscalYearBudget;
import org.devgateway.toolkit.persistence.repository.form.FiscalYearBudgetRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.devgateway.toolkit.persistence.service.BaseJpaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author mpostelnicu
 */
@Service
@Transactional
public class FiscalYearBudgetServiceImpl extends BaseJpaServiceImpl<FiscalYearBudget>
        implements FiscalYearBudgetService {

    @Autowired
    private FiscalYearBudgetRepository repository;

    @Override
    protected BaseJpaRepository<FiscalYearBudget, Long> repository() {
        return repository;
    }

    @Override
    public FiscalYearBudget newInstance() {
        return new FiscalYearBudget();
    }

}
