package org.devgateway.toolkit.persistence.service.form;

import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.form.AbstractMakueniEntity;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.dao.form.Project;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess;
import org.devgateway.toolkit.persistence.repository.form.TenderProcessRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.devgateway.toolkit.persistence.repository.norepository.TextSearchableRepository;
import org.devgateway.toolkit.persistence.validator.validators.TenderProcessValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author idobre
 * @since 2019-04-17
 */
@Service
@Transactional(readOnly = true)
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

    @Override
    public BindingResult validate(TenderProcess tp, AbstractMakueniEntity e) {
        String status = null;
        try {
            if (!Objects.isNull(e)) {
                status = e.getStatus();
                e.setStatus(null);
            }
            DataBinder binder = new DataBinder(tp);
            binder.setValidator(new TenderProcessValidator());
            binder.validate();
            BindingResult results = binder.getBindingResult();
            return results;
        } finally {
            if (status != null) {
                e.setStatus(status);
            }
        }
    }

    @Transactional
    public <E extends AbstractMakueniEntity> Stream<E> nonDraft(Stream<E> input) {
        return input.filter(i -> !DBConstants.Status.DRAFT.equals(i.getStatus()));
    }

}

