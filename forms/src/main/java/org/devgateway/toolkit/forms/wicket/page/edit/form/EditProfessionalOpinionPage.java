package org.devgateway.toolkit.forms.wicket.page.edit.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.RangeValidator;
import org.devgateway.toolkit.forms.wicket.components.form.FileInputBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.Select2ChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.components.util.SessionUtil;
import org.devgateway.toolkit.forms.wicket.providers.GenericChoiceProvider;
import org.devgateway.toolkit.persistence.dao.categories.Supplier;
import org.devgateway.toolkit.persistence.dao.form.Bid;
import org.devgateway.toolkit.persistence.dao.form.ProfessionalOpinion;
import org.devgateway.toolkit.persistence.dao.form.TenderQuotationEvaluation;
import org.devgateway.toolkit.persistence.service.form.ProcurementPlanService;
import org.devgateway.toolkit.persistence.service.form.ProfessionalOpinionService;
import org.devgateway.toolkit.persistence.service.form.TenderQuotationEvaluationService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.ArrayList;
import java.util.List;

/**
 * @author idobre
 * @since 2019-04-24
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
@MountPath("/professionalOpinion")
public class EditProfessionalOpinionPage extends EditAbstractMakueniEntityPage<ProfessionalOpinion> {
    @SpringBean
    protected ProfessionalOpinionService professionalOpinionService;

    @SpringBean
    protected ProcurementPlanService procurementPlanService;

    @SpringBean
    protected TenderQuotationEvaluationService tenderQuotationEvaluationService;

    private Select2ChoiceBootstrapFormComponent<Supplier> awardeeSelector;

    private final TenderQuotationEvaluation tenderQuotationEvaluation;

    public EditProfessionalOpinionPage(final PageParameters parameters) {
        super(parameters);
        this.jpaService = professionalOpinionService;

        this.tenderQuotationEvaluation = SessionUtil.getSessionTenderQuotationEvaluation();
        // TODO - check if this is a new object and without a tenderQuotationEvaluation,
        //  then redirect to some page like StatusOverview
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        ComponentUtil.addSelect2ChoiceField(editForm, "procurementPlan", procurementPlanService).required();

        final Select2ChoiceBootstrapFormComponent<TenderQuotationEvaluation> tenderQuotationEvaluation = ComponentUtil
                .addSelect2ChoiceField(editForm, "tenderQuotationEvaluation", tenderQuotationEvaluationService);
        tenderQuotationEvaluation.required();
        tenderQuotationEvaluation.getField().add(new TenderQuotationEvaluationAjaxComponentUpdatingBehavior("change"));

        awardeeSelector = new Select2ChoiceBootstrapFormComponent<>("awardee",
                new GenericChoiceProvider<>(getSuppliersInTenderQuotation()));
        awardeeSelector.required();
        editForm.add(awardeeSelector);

        ComponentUtil.addDateField(editForm, "professionalOpinionDate").required();

        final TextFieldBootstrapFormComponent<Double> recommendedAwardAmount = ComponentUtil.addDoubleField(editForm,
                "recommendedAwardAmount");
        recommendedAwardAmount.required();
        recommendedAwardAmount.getField().add(RangeValidator.minimum(0.0));

        ComponentUtil.addDateField(editForm, "approvedDate").required();

        final FileInputBootstrapFormComponent formDocs = new FileInputBootstrapFormComponent("formDocs");
        formDocs.required();
        editForm.add(formDocs);
    }

    @Override
    protected ProfessionalOpinion newInstance() {
        final ProfessionalOpinion professionalOpinion = super.newInstance();
        if (tenderQuotationEvaluation != null) {
            professionalOpinion.setProcurementPlan(tenderQuotationEvaluation.getProcurementPlan());
            professionalOpinion.setTenderQuotationEvaluation(tenderQuotationEvaluation);
        }
        return professionalOpinion;
    }

    private List<Supplier> getSuppliersInTenderQuotation() {
        TenderQuotationEvaluation tenderQuotationEvaluation = editForm.getModelObject().getTenderQuotationEvaluation();
        List<Supplier> suppliers = new ArrayList<>();
        if (tenderQuotationEvaluation != null && tenderQuotationEvaluation.getBids() != null) {
            for (Bid bid : tenderQuotationEvaluation.getBids()) {
                suppliers.add(bid.getSupplier());
            }
        }

        return suppliers;
    }

    class TenderQuotationEvaluationAjaxComponentUpdatingBehavior extends AjaxFormComponentUpdatingBehavior {
        TenderQuotationEvaluationAjaxComponentUpdatingBehavior(final String event) {
            super(event);
        }

        @Override
        protected void onUpdate(final AjaxRequestTarget target) {
            final ProfessionalOpinion professionalOpinion = editForm.getModelObject();
            professionalOpinion.setAwardee(null);
            awardeeSelector.provider(new GenericChoiceProvider<>(getSuppliersInTenderQuotation()));
            target.add(awardeeSelector);
        }
    }
}
