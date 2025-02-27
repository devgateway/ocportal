package org.devgateway.toolkit.persistence.service.form;


import org.devgateway.toolkit.persistence.dao.form.InspectionReport;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess;
import org.devgateway.toolkit.persistence.repository.form.InspectionReportRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author mpostelnicu
 */
@Service
@Transactional
public class InspectionReportServiceImpl extends AbstractImplTenderProcessClientEntityServiceImpl<InspectionReport>
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
    public List<InspectionReport> findByTenderProcess(final TenderProcess tenderProcess) {
        return repository.findByTenderProcess(tenderProcess);
    }
}
