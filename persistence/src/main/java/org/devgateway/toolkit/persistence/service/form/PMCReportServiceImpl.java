package org.devgateway.toolkit.persistence.service.form;


import org.devgateway.toolkit.persistence.dao.alerts.ApprovedReport;
import org.devgateway.toolkit.persistence.dao.form.PMCReport;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess;
import org.devgateway.toolkit.persistence.repository.alerts.ApprovedReportRepository;
import org.devgateway.toolkit.persistence.repository.form.PMCReportRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.devgateway.toolkit.persistence.repository.norepository.TextSearchableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author mpostelnicu
 */
@Service
@Transactional
public class PMCReportServiceImpl extends AbstractMakueniEntityServiceImpl<PMCReport>
        implements PMCReportService {

    @Autowired
    private PMCReportRepository repository;

    @Autowired
    private ApprovedReportRepository approvedReportRepository;

    @Override
    protected BaseJpaRepository<PMCReport, Long> repository() {
        return repository;
    }

    @Override
    public PMCReport newInstance() {
        return new PMCReport();
    }

    @Override
    public List<PMCReport> findByTenderProcess(final TenderProcess tenderProcess) {
        return repository.findByTenderProcess(tenderProcess);
    }

    @Override
    public TextSearchableRepository<PMCReport, Long> textRepository() {
        return repository;
    }

    @Override
    public void onApproved(PMCReport report) {
        ApprovedReport approvedReport = new ApprovedReport();
        approvedReport.setPmcReport(report);

        approvedReportRepository.save(approvedReport);
    }
}
