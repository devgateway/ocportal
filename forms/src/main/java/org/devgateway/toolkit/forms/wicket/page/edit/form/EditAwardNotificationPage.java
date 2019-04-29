package org.devgateway.toolkit.forms.wicket.page.edit.form;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.components.form.FileInputBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.GenericSleepFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.Select2ChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.page.lists.form.ListAwardNotificationPage;
import org.devgateway.toolkit.forms.wicket.providers.GenericChoiceProvider;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.categories.Supplier;
import org.devgateway.toolkit.persistence.dao.form.AwardNotification;
import org.devgateway.toolkit.persistence.dao.form.TenderQuotationEvaluation;
import org.devgateway.toolkit.persistence.service.form.ProcurementPlanService;
import org.devgateway.toolkit.persistence.service.form.AwardNotificationService;
import org.devgateway.toolkit.persistence.service.form.TenderQuotationEvalutionService;
import org.devgateway.toolkit.persistence.dao.form.Bid;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author gmutuhu
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
@MountPath("/awardNotification")
public class EditAwardNotificationPage extends EditAbstractMakueniFormPage<AwardNotification> {
    @SpringBean
    protected AwardNotificationService awardNotificationService;

    @SpringBean
    protected ProcurementPlanService procurementPlanService;

    @SpringBean
    protected TenderQuotationEvalutionService tenderQuotationEvalutionService;

    private Select2ChoiceBootstrapFormComponent<Supplier> awardeeSelector;
    private GenericSleepFormComponent tenderTitle = null;
    private GenericSleepFormComponent tenderNumber = null;
    private GenericSleepFormComponent supplierID = null;
    private GenericSleepFormComponent supplierAddress = null;
    private Select2ChoiceBootstrapFormComponent<TenderQuotationEvaluation> tenderQuotationEvaluation = null;

    public EditAwardNotificationPage(final PageParameters parameters) {
        super(parameters);

        this.jpaService = awardNotificationService;
        this.listPageClass = ListAwardNotificationPage.class;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        ComponentUtil.addSelect2ChoiceField(editForm, "procurementPlan", procurementPlanService).required();
        ComponentUtil.addDoubleField(editForm, "tenderValue").required();
        ComponentUtil.addDateField(editForm, "awardDate").required();
        ComponentUtil.addIntegerTextField(editForm, "acknowledgementDays");
        addTenderInfo();
        addSupplierInfo();
        final FileInputBootstrapFormComponent formDocs = new FileInputBootstrapFormComponent("formDocs");
        formDocs.required();
        editForm.add(formDocs);
    }

    @Override
    protected AwardNotification newInstance() {
        final AwardNotification awardNotification = super.newInstance();
        return awardNotification;
    }

    // TODO: we wont need the sleep component for tenderNumber and tenderTitle -
    // this is just temporary since we are selecting the tender evaluation.
    // Ideally the tender evaluation of an Notification should set only once on
    // creation.s
    private void addTenderInfo() {
        tenderQuotationEvaluation = ComponentUtil.addSelect2ChoiceField(editForm, "tenderQuotationEvaluation",
                tenderQuotationEvalutionService);
        tenderQuotationEvaluation.required();
        tenderQuotationEvaluation.getField().add(new TenderQuotationEvaluationAjaxComponentUpdatingBehavior("change"));

        tenderTitle = new GenericSleepFormComponent<>("tenderNumber", (IModel<String>) () -> {
            if (tenderQuotationEvaluation.getModelObject() != null) {
                return tenderQuotationEvaluation.getModelObject().getTender().getTenderNumber();
            }
            return null;
        });
        tenderTitle.setOutputMarkupId(true);
        editForm.add(tenderTitle);

        tenderNumber = new GenericSleepFormComponent<>("tenderTitle", (IModel<String>) () -> {
            if (tenderQuotationEvaluation.getModelObject() != null) {
                return tenderQuotationEvaluation.getModelObject().getTender().getTenderTitle();
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

        supplierAddress = new GenericSleepFormComponent<>("supplierAddress", (IModel<String>) () -> {
            if (awardeeSelector.getModelObject() != null) {
                return awardeeSelector.getModelObject().getAddress();
            }
            return null;
        });
        supplierAddress.setOutputMarkupId(true);
        editForm.add(supplierAddress);
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
            final AwardNotification awardNotification = editForm.getModelObject();
            awardNotification.setAwardee(null);
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
            target.add(supplierAddress);
        }
    }
}
