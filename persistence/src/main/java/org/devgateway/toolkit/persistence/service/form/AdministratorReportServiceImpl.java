package org.devgateway.toolkit.persistence.service.form;


import org.devgateway.toolkit.persistence.dao.form.AdministratorReport;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess;
import org.devgateway.toolkit.persistence.repository.form.AdministratorReportRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.devgateway.toolkit.persistence.repository.norepository.TextSearchableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author mpostelnicu
 */
@Service
public class AdministratorReportServiceImpl extends AbstractMakueniEntityServiceImpl<AdministratorReport>
        implements AdministratorReportService {

    @Autowired
    private AdministratorReportRepository repository;

    @Override
    protected BaseJpaRepository<AdministratorReport, Long> repository() {
        return repository;
    }

    @Override
    public AdministratorReport newInstance() {
        return new AdministratorReport();
    }

    @Override
    @Cacheable
    public List<AdministratorReport> findByTenderProcess(final TenderProcess tenderProcess) {
        return repository.findByTenderProcess(tenderProcess);
    }

    @Override
    public TextSearchableRepository<AdministratorReport, Long> textRepository() {
        return repository;
    }
}
