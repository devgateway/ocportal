package org.devgateway.toolkit.forms.wicket.page.edit.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.RangeValidator;
import org.devgateway.toolkit.forms.wicket.components.form.GenericSleepFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.Select2ChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.page.edit.panel.ContractDocumentPanel;
import org.devgateway.toolkit.forms.wicket.providers.GenericChoiceProvider;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.categories.Supplier;
import org.devgateway.toolkit.persistence.dao.form.Bid;
import org.devgateway.toolkit.persistence.dao.form.Contract;
import org.devgateway.toolkit.persistence.dao.form.TenderQuotationEvaluation;
import org.devgateway.toolkit.persistence.service.category.ProcuringEntityService;
import org.devgateway.toolkit.persistence.service.form.ContractService;
import org.devgateway.toolkit.persistence.service.form.ProcurementPlanService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gmutuhu
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
@MountPath("/contract")
public class EditContractPage extends EditAbstractTenderReqMakueniEntity<Contract> {
    @SpringBean
    protected ContractService contractService;

    @SpringBean
    protected ProcurementPlanService procurementPlanService;

    @SpringBean
    protected ProcuringEntityService procuringEntityService;

    private Select2ChoiceBootstrapFormComponent<Supplier> awardeeSelector;

    private GenericSleepFormComponent supplierAddress;

    public EditContractPage(final PageParameters parameters) {
        super(parameters);
        this.jpaService = contractService;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();             
        ComponentUtil.addTextField(editForm, "referenceNumber").required();
        ComponentUtil.addDoubleField(editForm, "tenderValue").required()
                .getField().add(RangeValidator.minimum(0.0));
        ComponentUtil.addDateField(editForm, "contractApprovalDate").required();
        ComponentUtil.addDateField(editForm, "contractDate").required();
        ComponentUtil.addDateField(editForm, "expiryDate");
        ComponentUtil.addSelect2ChoiceField(editForm, "procuringEntity", procuringEntityService).required();

        addSupplierInfo();

        editForm.add(new ContractDocumentPanel("contractDocs"));
    }

    @Override
    protected Contract newInstance() {
        final Contract contract = super.newInstance();
        contract.setPurchaseRequisition(purchaseRequisition);
        return contract;
    }


    private void addSupplierInfo() {
        awardeeSelector = new Select2ChoiceBootstrapFormComponent<>("awardee",
                new GenericChoiceProvider<>(getSuppliersInTenderQuotation()));
        awardeeSelector.required();
        awardeeSelector.getField().add(new AwardeeAjaxComponentUpdatingBehavior("change"));
        editForm.add(awardeeSelector);

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
        TenderQuotationEvaluation tenderQuotationEvaluation = purchaseRequisition.getTenderQuotationEvaluation();
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

    class AwardeeAjaxComponentUpdatingBehavior extends AjaxFormComponentUpdatingBehavior {
        AwardeeAjaxComponentUpdatingBehavior(final String event) {
            super(event);
        }

        @Override
        protected void onUpdate(final AjaxRequestTarget target) {
            target.add(supplierAddress);
        }
    }
}
