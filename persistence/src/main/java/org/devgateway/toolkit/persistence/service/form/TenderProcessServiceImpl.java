package org.devgateway.toolkit.persistence.service.form;

import com.google.common.collect.ImmutableList;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.dao.form.AbstractMakueniEntity;
import org.devgateway.toolkit.persistence.dao.form.AwardAcceptance;
import org.devgateway.toolkit.persistence.dao.form.AwardNotification;
import org.devgateway.toolkit.persistence.dao.form.Contract;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.dao.form.ProfessionalOpinion;
import org.devgateway.toolkit.persistence.dao.form.Project;
import org.devgateway.toolkit.persistence.dao.form.Tender;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess;
import org.devgateway.toolkit.persistence.dao.form.TenderQuotationEvaluation;
import org.devgateway.toolkit.persistence.fm.service.DgFmService;
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

    private static class TenderProcessForm {

        private final Class<?> formClass;

        private final String featureName;

        TenderProcessForm(Class<?> formClass, String featureName) {
            this.formClass = formClass;
            this.featureName = featureName;
        }

        public Class<?> getFormClass() {
            return formClass;
        }

        public String getFeatureName() {
            return featureName;
        }
    }

    private static final List<TenderProcessForm> FORMS = ImmutableList.of(
            // this is really for purchase reqs
            new TenderProcessForm(TenderProcess.class, "tenderProcessForm.purchRequisitions"),
            new TenderProcessForm(Tender.class, "tenderForm"),
            new TenderProcessForm(TenderQuotationEvaluation.class, "tenderQuotationEvaluationForm"),
            new TenderProcessForm(ProfessionalOpinion.class, "professionalOpinionForm"),
            new TenderProcessForm(AwardNotification.class, "awardNotificationForm"),
            new TenderProcessForm(AwardAcceptance.class, "awardAcceptanceForm"),
            new TenderProcessForm(Contract.class, "contractForm")
    );

    @Autowired
    private TenderProcessRepository tenderProcessRepository;

    @Autowired
    private DgFmService dgFmService;

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
            binder.setValidator(createValidator(e));
            binder.validate();
            return binder.getBindingResult();
        } finally {
            if (status != null) {
                e.setStatus(status);
            }
        }
    }

    @Override
    public Long countByFiscalYear(FiscalYear fiscalYear) {
        return tenderProcessRepository.countByFiscalYear(fiscalYear);
    }

    @Override
    public Long countByDepartmentAndFiscalYear(Department department, FiscalYear fiscalYear) {
        return tenderProcessRepository.countByDepartmentAndFiscalYear(department, fiscalYear);
    }

    private TenderProcessValidator createValidator(AbstractMakueniEntity e) {
        return new TenderProcessValidator(e != null);
    }

    @Override
    public BindingResult validate(TenderProcess tp) {
        return validate(tp, null);
    }

    @Transactional
    public <E extends AbstractMakueniEntity> Stream<E> nonDraft(Stream<E> input) {
        return input.filter(i -> !DBConstants.Status.DRAFT.equals(i.getStatus()));
    }

    @Override
    public Class<?> getFirstVisibleDownstreamForm(Class<?> formClass) {
        boolean canReturn = false;
        for (TenderProcessForm form : FORMS) {
            if (form.getFormClass().equals(formClass)) {
                canReturn = true;
            }
            if (canReturn && dgFmService.isFeatureVisible(form.getFeatureName())) {
                return form.getFormClass();
            }
        }
        return null;
    }
}

