package org.devgateway.toolkit.forms.wicket.page.edit.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.RangeValidator;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.validators.BigDecimalValidator;
import org.devgateway.toolkit.forms.wicket.components.form.FileInputBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.GenericSleepFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.Select2ChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.page.BasePage;
import org.devgateway.toolkit.forms.wicket.providers.GenericChoiceProvider;
import org.devgateway.toolkit.persistence.dao.categories.Supplier;
import org.devgateway.toolkit.persistence.dao.form.AwardAcceptance;
import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisition;
import org.devgateway.toolkit.persistence.service.form.AwardAcceptanceService;
import org.devgateway.toolkit.persistence.service.form.PurchaseRequisitionService;
import org.devgateway.toolkit.persistence.spring.PersistenceUtil;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.springframework.util.ObjectUtils;
import org.wicketstuff.annotation.mount.MountPath;

import java.math.BigDecimal;

/**
 * @author gmutuhu
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
@MountPath
public class EditAwardAcceptancePage extends EditAbstractTenderReqMakueniEntity<AwardAcceptance> {
    @SpringBean
    protected AwardAcceptanceService awardAcceptanceService;

    @SpringBean
    protected PurchaseRequisitionService purchaseRequisitionService;

    private Select2ChoiceBootstrapFormComponent<Supplier> awardeeSelector;

    private GenericSleepFormComponent supplierID;

    public EditAwardAcceptancePage(final PageParameters parameters) {
        super(parameters);
        this.jpaService = awardAcceptanceService;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        ComponentUtil.addBigDecimalField(editForm, "acceptedAwardValue").required()
                .getField().add(RangeValidator.minimum(BigDecimal.ZERO), new BigDecimalValidator());
        ComponentUtil.addDateField(editForm, "acceptanceDate").required();

        addSupplierInfo();

        final FileInputBootstrapFormComponent formDocs = new FileInputBootstrapFormComponent("formDocs");
        formDocs.required();
        editForm.add(formDocs);
    }

    @Override
    protected AwardAcceptance newInstance() {
        final AwardAcceptance awardAcceptance = super.newInstance();
        awardAcceptance.setPurchaseRequisition(sessionMetadataService.getSessionPurchaseRequisition());

        return awardAcceptance;
    }

    @Override
    protected void beforeSaveEntity(final AwardAcceptance awardAcceptance) {
        super.beforeSaveEntity(awardAcceptance);

        final PurchaseRequisition purchaseRequisition = awardAcceptance.getPurchaseRequisition();
        purchaseRequisition.addAwardAcceptance(awardAcceptance);
        purchaseRequisitionService.save(purchaseRequisition);
    }

    @Override
    protected void beforeDeleteEntity(final AwardAcceptance awardAcceptance) {
        super.beforeDeleteEntity(awardAcceptance);

        final PurchaseRequisition purchaseRequisition = awardAcceptance.getPurchaseRequisition();
        purchaseRequisition.removeAwardAcceptance(awardAcceptance);
        purchaseRequisitionService.save(purchaseRequisition);
    }

    @Override
    protected Class<? extends BasePage> pageAfterSubmitAndNext() {
        return EditContractPage.class;
    }

    @Override
    protected PageParameters parametersAfterSubmitAndNext() {
        final PageParameters pp = new PageParameters();
        if (!ObjectUtils.isEmpty(editForm.getModelObject().getPurchaseRequisition().getContract())) {
            pp.set(WebConstants.PARAM_ID,
                    PersistenceUtil.getNext(
                            editForm.getModelObject().getPurchaseRequisition().getContract()).getId());
        }

        return pp;
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
