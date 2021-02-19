package org.devgateway.toolkit.persistence.service.form;


import org.devgateway.toolkit.persistence.dao.alerts.ApprovedReport;
import org.devgateway.toolkit.persistence.dao.categories.Department_;
import org.devgateway.toolkit.persistence.dao.form.PMCReport;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess;
import org.devgateway.toolkit.persistence.repository.alerts.ApprovedReportRepository;
import org.devgateway.toolkit.persistence.repository.form.PMCReportRepository;
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
public class PMCReportServiceImpl extends AbstractImplTenderProcessMakueniEntityServiceImpl<PMCReport>
        implements PMCReportService {

    @Autowired
    private PMCReportRepository repository;

    @Autowired
    private TenderProcessService tenderProcessService;

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
    public List<PMCReport> getPMCReportsCreatedBy(String username) {
        return repository.findAll((r, cq, cb) -> cb.and(r.get(Department_.createdBy).in(username)));
    }

    @Override
    public PMCReport saveReportAndUpdateTenderProcess(PMCReport entity) {
        TenderProcess tenderProcess = entity.getTenderProcess();
        tenderProcess.addPMCReport(entity);
        tenderProcessService.save(tenderProcess);
        return save(entity);
    }

    @Override
    public void onApproved(PMCReport report) {
        ApprovedReport approvedReport = new ApprovedReport();
        approvedReport.setPmcReport(report);

        approvedReportRepository.save(approvedReport);
    }

    @Override
    public void delete(PMCReport report) {
        approvedReportRepository.deleteByPmcReport(report);
        super.delete(report);
    }
}
