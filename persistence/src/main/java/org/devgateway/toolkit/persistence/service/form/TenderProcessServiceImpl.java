package org.devgateway.toolkit.persistence.service.form;

import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.dao.form.Project;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess;
import org.devgateway.toolkit.persistence.repository.form.TenderProcessRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.devgateway.toolkit.persistence.repository.norepository.TextSearchableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

/**
 * @author idobre
 * @since 2019-04-17
 */
@Service
@Transactional
public class TenderProcessServiceImpl extends AbstractMakueniEntityServiceImpl<TenderProcess>
        implements TenderProcessService {
    @Autowired
    private TenderProcessRepository tenderProcessRepository;

    @Override
    protected BaseJpaRepository<TenderProcess, Long> repository() {
        return tenderProcessRepository;
    }

    @Override
    public TextSearchableRepository<TenderProcess, Long> textRepository() {
        return tenderProcessRepository;
    }


    @Override
    public TenderProcess newInstance() {
        return new TenderProcess();
    }

    @Override
    public List<TenderProcess> findByProject(final Project project) {
        return tenderProcessRepository.findByProject(project);
    }

    @Override
    public List<TenderProcess> findByProjectProcurementPlan(final ProcurementPlan procurementPlan) {
        return tenderProcessRepository.findByProjectProcurementPlan(procurementPlan);
    }

    @Override
    public Stream<TenderProcess> findAllStream() {
        return tenderProcessRepository.findAllStream();
    }
}

