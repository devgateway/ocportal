package org.devgateway.toolkit.persistence.service.form;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
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
import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisitionGroup;
import org.devgateway.toolkit.persistence.dao.form.Tender;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess;
import org.devgateway.toolkit.persistence.dao.form.TenderQuotationEvaluation;
import org.devgateway.toolkit.persistence.fm.service.DgFmService;
import org.devgateway.toolkit.persistence.repository.form.TenderProcessRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.devgateway.toolkit.persistence.service.BaseJpaServiceImpl;
import org.devgateway.toolkit.persistence.validator.validators.TenderProcessValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author idobre
 * @since 2019-04-17
 */
@Service
@Transactional
public class TenderProcessServiceImpl extends BaseJpaServiceImpl<TenderProcess>
        implements TenderProcessService {

    private static class TenderProcessForm {

        private final Class<? extends AbstractMakueniEntity> formClass;

        private final String featureName;

        TenderProcessForm(Class<? extends AbstractMakueniEntity> formClass, String featureName) {
            this.formClass = formClass;
            this.featureName = featureName;
        }

        public Class<? extends AbstractMakueniEntity> getFormClass() {
            return formClass;
        }

        public String getFeatureName() {
            return featureName;
        }
    }

    @Override
    public AbstractMakueniEntity getPreviousStatusable(TenderProcess tp, Class<?> currentClazz) {
        TenderProcessForm entry = FORMS.stream().filter(f -> f.getFormClass().equals(currentClazz))
                .findFirst().orElseThrow(() -> new RuntimeException("Unknown class to fm mapping " + currentClazz));
        for (int i = FORMS.indexOf(entry) - 1; i >= 0; i--) {
            if (dgFmService.isFeatureVisible(FORMS.get(i).getFeatureName())) {
                return tp.getProcurementEntity(FORMS.get(i).getFormClass());
            }
        }
        return null;
    }

    @Override
    public AbstractMakueniEntity getNextStatusable(TenderProcess tp, Class<?> currentClazz) {
        TenderProcessForm entry = FORMS.stream().filter(f -> f.getFormClass().equals(currentClazz))
                .findFirst().orElseThrow(() -> new RuntimeException("Unknown class to fm mapping " + currentClazz));
        for (int i = FORMS.indexOf(entry) + 1; i < FORMS.size(); i++) {
            if (dgFmService.isFeatureVisible(FORMS.get(i).getFeatureName())) {
                return tp.getProcurementEntity(FORMS.get(i).getFormClass());
            }
        }
        return null;
    }

    @Override
    public List<TenderProcess> findByFiscalYear(final FiscalYear fiscalYear) {
        return tenderProcessRepository.findByFiscalYear(fiscalYear);
    }

    public static final List<TenderProcessForm> FORMS = ImmutableList.of(
            // this is really for purchase reqs
            new TenderProcessForm(ProcurementPlan.class, "procurementPlanForm"),
            new TenderProcessForm(Project.class, "projectForm"),
            new TenderProcessForm(PurchaseRequisitionGroup.class, "purchaseRequisitionForm"),
            new TenderProcessForm(Tender.class, "tenderForm"),
            new TenderProcessForm(TenderQuotationEvaluation.class, "tenderQuotationEvaluationForm"),
            new TenderProcessForm(ProfessionalOpinion.class, "professionalOpinionForm"),
            new TenderProcessForm(AwardNotification.class, "awardNotificationForm"),
            new TenderProcessForm(AwardAcceptance.class, "awardAcceptanceForm"),
            new TenderProcessForm(Contract.class, "contractForm")
    );

    public static final Map<? extends Class<? extends AbstractMakueniEntity>, String> FORM_FM_MAP =
            ImmutableMap.copyOf(FORMS.stream().collect(Collectors.toMap(TenderProcessForm::getFormClass,
                    TenderProcessForm::getFeatureName)));

    @Autowired
    private TenderProcessRepository tenderProcessRepository;

    @Autowired
    private DgFmService dgFmService;

    @Override
    protected BaseJpaRepository<TenderProcess, Long> repository() {
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

