package org.devgateway.toolkit.persistence.service.form;


import org.devgateway.toolkit.persistence.dao.form.PMCReport;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess;
import org.devgateway.toolkit.persistence.repository.form.PMCReportRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author mpostelnicu
 */
@Service
@Transactional
public class PMCReportServiceImpl extends AbstractMakueniEntityServiceImpl<PMCReport>
        implements PMCReportService {

    @Autowired
    private PMCReportRepository repository;

    @Override
    protected BaseJpaRepository<PMCReport, Long> repository() {
        return repository;
    }

    @Override
    public PMCReport newInstance() {
        return new PMCReport();
    }

    @Override
    @Cacheable
    public PMCReport findByTenderProcess(final TenderProcess tenderProcess) {
        return repository.findByTenderProcess(tenderProcess);
    }
}
