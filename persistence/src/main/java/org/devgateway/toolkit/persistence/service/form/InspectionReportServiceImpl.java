package org.devgateway.toolkit.persistence.service.form;


import org.devgateway.toolkit.persistence.dao.form.InspectionReport;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess;
import org.devgateway.toolkit.persistence.repository.form.InspectionReportRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author gmutuhu
 */
@Service
@Transactional(readOnly = true)
public class InspectionReportServiceImpl extends AbstractMakueniEntityServiceImpl<InspectionReport>
        implements InspectionReportService {

    @Autowired
    private InspectionReportRepository repository;

    @Override
    protected BaseJpaRepository<InspectionReport, Long> repository() {
        return repository;
    }

    @Override
    public InspectionReport newInstance() {
        return new InspectionReport();
    }

    @Override
    @Cacheable
    public InspectionReport findByTenderProcess(final TenderProcess tenderProcess) {
        return repository.findByTenderProcess(tenderProcess);
    }

}
