package org.devgateway.toolkit.persistence.service.category;

import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.repository.category.FiscalYearRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.devgateway.toolkit.persistence.repository.norepository.TextSearchableRepository;
import org.devgateway.toolkit.persistence.service.BaseJpaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author mpostelnicu
 */
@Service
@Transactional(readOnly = true)
public class FiscalYearServiceImpl extends BaseJpaServiceImpl<FiscalYear> implements FiscalYearService {
    @Autowired
    private FiscalYearRepository fiscalYearRepository;

    @Override
    protected BaseJpaRepository<FiscalYear, Long> repository() {
        return fiscalYearRepository;
    }

    @Override
    public TextSearchableRepository<FiscalYear, Long> textRepository() {
        return fiscalYearRepository;
    }

    @Override
    public FiscalYear newInstance() {
        return new FiscalYear();
    }

    @Override
    @Cacheable
    public FiscalYear getLastFiscalYear() {
        return fiscalYearRepository.findTopByOrderByStartDateDesc();
    }

    // TODO: limit to only years with data
    @Override
    @Cacheable
    public List<FiscalYear> getYearsWithData() {
        return fiscalYearRepository.findAllByOrderByStartDateDesc();
    }

    @Override
    @Cacheable
    public List<FiscalYear> getYearsWithData(final Long departmentId) {
        return fiscalYearRepository.getYearsWithData(departmentId);
    }
}
