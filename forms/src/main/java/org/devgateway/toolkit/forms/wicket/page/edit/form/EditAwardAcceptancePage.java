package org.devgateway.toolkit.forms.wicket.page.edit.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.apache.wicket.validation.validator.RangeValidator;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.wicket.components.form.FileInputBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.GenericSleepFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.Select2ChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.page.lists.form.ListAwardAcceptancePage;
import org.devgateway.toolkit.forms.wicket.providers.GenericChoiceProvider;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.categories.Supplier;
import org.devgateway.toolkit.persistence.dao.form.AwardAcceptance;
import org.devgateway.toolkit.persistence.dao.form.Bid;
import org.devgateway.toolkit.persistence.dao.form.TenderQuotationEvaluation;
import org.devgateway.toolkit.persistence.service.form.AwardAcceptanceService;
import org.devgateway.toolkit.persistence.service.form.ProcurementPlanService;
import org.devgateway.toolkit.persistence.service.form.TenderQuotationEvaluationService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gmutuhu
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
@MountPath("/awardAcceptance")
public class EditAwardAcceptancePage extends EditAbstractMakueniFormPage<AwardAcceptance> {
    @SpringBean
    protected AwardAcceptanceService awardAcceptanceService;

    @SpringBean
    protected ProcurementPlanService procurementPlanService;

    @SpringBean
    protected TenderQuotationEvaluationService tenderQuotationEvaluationService;

    private Select2ChoiceBootstrapFormComponent<Supplier> awardeeSelector;
    private GenericSleepFormComponent tenderTitle = null;
    private GenericSleepFormComponent tenderNumber = null;
    private GenericSleepFormComponent supplierID = null;
    private Select2ChoiceBootstrapFormComponent<TenderQuotationEvaluation> tenderQuotationEvaluationSelector = null;
    private TenderQuotationEvaluation tenderQuotationEvaluation;

    public EditAwardAcceptancePage(final PageParameters parameters) {
        super(parameters);
        this.jpaService = awardAcceptanceService;
        this.listPageClass = ListAwardAcceptancePage.class;

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
        ComponentUtil.addDoubleField(editForm, "tenderValue")
                .getField().add(RangeValidator.minimum(0.0));
        ComponentUtil.addDateField(editForm, "acceptanceDate").required();

        addTenderInfo();
        addSupplierInfo();

        final FileInputBootstrapFormComponent formDocs = new FileInputBootstrapFormComponent("formDocs");
        formDocs.required();
        editForm.add(formDocs);
    }

    @Override
    protected AwardAcceptance newInstance() {
        final AwardAcceptance awardAcceptance = super.newInstance();
        if (tenderQuotationEvaluation != null) {
            awardAcceptance.setProcurementPlan(tenderQuotationEvaluation.getProcurementPlan());
            awardAcceptance.setTenderQuotationEvaluation(tenderQuotationEvaluation);
        }
        return awardAcceptance;
    }

    // TODO: we wont need the sleep component for tenderNumber and tenderTitle -
    // this is just temporary since we are selecting the tender evaluation.
    // Ideally the tender evaluation of an Notification should set only once on
    // creation.s
    private void addTenderInfo() {
        tenderQuotationEvaluationSelector = ComponentUtil.addSelect2ChoiceField(editForm, "tenderQuotationEvaluation",
                tenderQuotationEvaluationService);
        tenderQuotationEvaluationSelector.required();
        tenderQuotationEvaluationSelector.getField()
                .add(new TenderQuotationEvaluationAjaxComponentUpdatingBehavior("change"));

        tenderTitle = new GenericSleepFormComponent<>("tenderNumber", (IModel<String>) () -> {
            if (tenderQuotationEvaluationSelector.getModelObject() != null) {
                return tenderQuotationEvaluationSelector.getModelObject().getTender().getTenderNumber();
            }
            return null;
        });
        tenderTitle.setOutputMarkupId(true);
        editForm.add(tenderTitle);

        tenderNumber = new GenericSleepFormComponent<>("tenderTitle", (IModel<String>) () -> {
            if (tenderQuotationEvaluationSelector.getModelObject() != null) {
                return tenderQuotationEvaluationSelector.getModelObject().getTender().getTenderTitle();
            }
            return null;
        });
        tenderNumber.setOutputMarkupId(true);
        editForm.add(tenderNumber);
    }

    private void addSupplierInfo() {
        awardeeSelector = new Select2ChoiceBootstrapFormComponent<>("awardee",
                new GenericChoiceProvider<>(getSuppliersInTenderQuotation()));
        awardeeSelector.required();
        awardeeSelector.getField().add(new AwardeeAjaxComponentUpdatingBehavior("change"));
        editForm.add(awardeeSelector);

        supplierID = new GenericSleepFormComponent<>("supplierID", (IModel<String>) () -> {
            if (awardeeSelector.getModelObject() != null) {
                return awardeeSelector.getModelObject().getCode();
            }
            return null;
        });
        supplierID.setOutputMarkupId(true);
        editForm.add(supplierID);

    }

    private List<Supplier> getSuppliersInTenderQuotation() {
        TenderQuotationEvaluation tenderQuotationEvaluation = editForm.getModelObject().getTenderQuotationEvaluation();
        List<Supplier> suppliers = new ArrayList<>();
        if (tenderQuotationEvaluation != null && tenderQuotationEvaluation.getBids() != null) {
            for (Bid bid : tenderQuotationEvaluation.getBids()) {
                if (DBConstants.SupplierResponsiveness.PASS.equalsIgnoreCase(bid.getSupplierResponsiveness())) {
                    suppliers.add(bid.getSupplier());
                }
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
            final AwardAcceptance awardAcceptance = editForm.getModelObject();
            awardAcceptance.setAwardee(null);
            awardeeSelector.provider(new GenericChoiceProvider<>(getSuppliersInTenderQuotation()));
            target.add(awardeeSelector);
            target.add(tenderNumber);
            target.add(tenderTitle);
        }
    }

    class AwardeeAjaxComponentUpdatingBehavior extends AjaxFormComponentUpdatingBehavior {
        AwardeeAjaxComponentUpdatingBehavior(final String event) {
            super(event);
        }

        @Override
        protected void onUpdate(final AjaxRequestTarget target) {
            target.add(supplierID);
        }
    }
}
