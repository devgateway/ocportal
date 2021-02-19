package org.devgateway.toolkit.persistence.service.category;

import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear_;
import org.devgateway.toolkit.persistence.repository.category.FiscalYearRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.devgateway.toolkit.persistence.service.BaseJpaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.metamodel.SingularAttribute;
import java.util.List;

/**
 * @author mpostelnicu
 */
@Service
@Transactional(readOnly = true)
public class FiscalYearServiceImpl extends BaseJpaServiceImpl<FiscalYear> implements FiscalYearService {
    @Autowired
    private FiscalYearRepository fiscalYearRepository;

    @Resource
    private FiscalYearService self; // Self-autowired reference to proxified bean of this class.

    @Override
    protected BaseJpaRepository<FiscalYear, Long> repository() {
        return fiscalYearRepository;
    }

    @Override
    public FiscalYear newInstance() {
        return new FiscalYear();
    }

    public List<FiscalYear> getAll() {
        return self.findAll();
    }

    @Override
    public FiscalYear findByName(final String name) {
        return fiscalYearRepository.findByName(name);
    }

    @Override
    public FiscalYear getLastFiscalYear() {
        return self.getYearsWithData().isEmpty() ? null : self.getYearsWithData().get(0);
    }

    @Override
    public List<FiscalYear> getYearsWithData() {
        return fiscalYearRepository.getYearsWithData();
    }

    @Override
    public SingularAttribute<? super FiscalYear, String> getTextAttribute() {
        return FiscalYear_.name;
    }
}
