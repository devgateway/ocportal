package org.devgateway.toolkit.forms.wicket.page.edit.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.apache.wicket.validation.validator.RangeValidator;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.wicket.components.form.FileInputBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.Select2ChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.page.lists.form.ListProfessionalOpinionPage;
import org.devgateway.toolkit.forms.wicket.providers.GenericChoiceProvider;
import org.devgateway.toolkit.persistence.dao.categories.Supplier;
import org.devgateway.toolkit.persistence.dao.form.Bid;
import org.devgateway.toolkit.persistence.dao.form.ProfessionalOpinion;
import org.devgateway.toolkit.persistence.dao.form.TenderQuotationEvaluation;
import org.devgateway.toolkit.persistence.service.category.SupplierService;
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
public class EditProfessionalOpinionPage extends EditAbstractMakueniFormPage<ProfessionalOpinion> {
    @SpringBean
    protected ProfessionalOpinionService professionalOpinionService;

    @SpringBean
    protected ProcurementPlanService procurementPlanService;

    @SpringBean
    protected SupplierService supplierService;

    @SpringBean
    protected TenderQuotationEvaluationService tenderQuotationEvaluationService;

    private Select2ChoiceBootstrapFormComponent<Supplier> awardeeSelector;

    private TenderQuotationEvaluation tenderQuotationEvaluation;

    public EditProfessionalOpinionPage(final PageParameters parameters) {
        super(parameters);

        this.jpaService = professionalOpinionService;
        this.listPageClass = ListProfessionalOpinionPage.class;

        StringValue tenderOpeningId = parameters.get(WebConstants.PARAM_TENDER_OPENING_ID);
        if (!tenderOpeningId.isNull()) {
            tenderQuotationEvaluation = tenderQuotationEvaluationService.findById(tenderOpeningId.toLong())
                    .orElse(null);
        }
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
        recommendedAwardAmount.getField().add(new RangeValidator(0.0, null));

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
