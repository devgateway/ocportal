package org.devgateway.toolkit.persistence.service.form;


import java.util.List;

import org.devgateway.toolkit.persistence.dao.categories.Department_;
import org.devgateway.toolkit.persistence.dao.form.PMCReport;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess;
import org.devgateway.toolkit.persistence.repository.form.PMCReportRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.devgateway.toolkit.persistence.repository.norepository.TextSearchableRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private TenderProcessService tenderProcessService;

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
}
