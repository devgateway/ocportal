package org.devgateway.toolkit.forms.wicket.page.edit.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.RangeValidator;
import org.devgateway.toolkit.forms.validators.AfterThanDateValidator;
import org.devgateway.toolkit.forms.validators.BigDecimalValidator;
import org.devgateway.toolkit.forms.wicket.behaviors.CountyAjaxFormComponentUpdatingBehavior;
import org.devgateway.toolkit.forms.wicket.components.form.DateFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.GenericSleepFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.Select2ChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.Select2MultiChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.page.edit.panel.ContractDocumentPanel;
import org.devgateway.toolkit.forms.wicket.page.edit.roleassignable.ProcurementRoleAssignable;
import org.devgateway.toolkit.forms.wicket.providers.EntityListChoiceProvider;
import org.devgateway.toolkit.forms.wicket.providers.GenericChoiceProvider;
import org.devgateway.toolkit.persistence.dao.categories.Subcounty;
import org.devgateway.toolkit.persistence.dao.categories.Supplier;
import org.devgateway.toolkit.persistence.dao.categories.TargetGroup;
import org.devgateway.toolkit.persistence.dao.categories.Ward;
import org.devgateway.toolkit.persistence.dao.form.AwardAcceptanceItem;
import org.devgateway.toolkit.persistence.dao.form.AwardNotificationItem;
import org.devgateway.toolkit.persistence.dao.form.Contract;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess;
import org.devgateway.toolkit.persistence.service.category.SubcountyService;
import org.devgateway.toolkit.persistence.service.category.WardService;
import org.devgateway.toolkit.persistence.service.form.ContractService;
import org.devgateway.toolkit.persistence.service.form.TenderProcessService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author gmutuhu
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
@MountPath
public class EditContractPage extends EditAbstractTenderReqMakueniEntityPage<Contract> implements
        ProcurementRoleAssignable {
    @SpringBean
    protected ContractService contractService;

    @SpringBean
    protected TenderProcessService tenderProcessService;

    @SpringBean
    protected SubcountyService subcountyService;

    @SpringBean
    protected WardService wardService;

    private Select2ChoiceBootstrapFormComponent<Supplier> awardeeSelector;
    private Select2MultiChoiceBootstrapFormComponent<Ward> wards;
    private Select2MultiChoiceBootstrapFormComponent<Subcounty> subcounties;

    private GenericSleepFormComponent<String> supplierAddress;
    private Select2ChoiceBootstrapFormComponent<TargetGroup> targetGroup;

    public EditContractPage() {
        this(new PageParameters());
    }

    public EditContractPage(final PageParameters parameters) {
        super(parameters);
        this.jpaService = contractService;
    }

    @Override
    protected void onInitialize() {
        editForm.attachFm("contractForm");
        super.onInitialize();

        submitAndNext.setVisibilityAllowed(false);

        ComponentUtil.addTextField(editForm, "referenceNumber");
        ComponentUtil.addBigDecimalField(editForm, "contractValue")
                .getField().add(RangeValidator.minimum(BigDecimal.ZERO), new BigDecimalValidator());

        final DateFieldBootstrapFormComponent contractDate = ComponentUtil.addDateField(editForm, "contractDate");

        AwardNotificationItem acceptedNotification = editForm.getModelObject().getTenderProcess()
                .getSingleAwardNotification().getAcceptedNotification();

        if (acceptedNotification != null && acceptedNotification.getAwardDate() != null) {
            contractDate.getField().add(new AfterThanDateValidator(acceptedNotification.getAwardDate()));
        }

        ComponentUtil.addDateField(editForm, "contractApprovalDate");
        ComponentUtil.addDateField(editForm, "expiryDate");

        addSupplierInfo();

        editForm.add(new ContractDocumentPanel("contractDocs"));

        wards = ComponentUtil.addSelect2MultiChoiceField(editForm, "wards", wardService);
        subcounties = ComponentUtil.addSelect2MultiChoiceField(editForm, "subcounties", subcountyService);
        subcounties.getField().add(new CountyAjaxFormComponentUpdatingBehavior<>(subcounties, wards,
                LoadableDetachableModel.of(() -> wardService), editForm.getModelObject()::setWards, "change"
        ));
    }

    @Override
    protected Contract newInstance() {
        final Contract contract = super.newInstance();
        contract.setTenderProcess(sessionMetadataService.getSessionTenderProcess());

        return contract;
    }

    @Override
    protected void beforeSaveEntity(final Contract contract) {
        super.beforeSaveEntity(contract);

        final TenderProcess tenderProcess = contract.getTenderProcess();
        tenderProcess.addContract(contract);
        tenderProcessService.save(tenderProcess);
    }

    @Override
    protected void beforeDeleteEntity(final Contract contract) {
        super.beforeDeleteEntity(contract);

        final TenderProcess tenderProcess = contract.getTenderProcess();
        tenderProcess.removeContract(contract);
        tenderProcessService.save(tenderProcess);
    }

    public static List<Supplier> getAcceptedSupplier(TenderProcess tenderProcess) {
        if (tenderProcess.getSingleAwardAcceptance() != null) {
            return tenderProcess.getSingleAwardAcceptance().getItems()
                    .stream()
                    .filter(AwardAcceptanceItem::isAccepted)
                    .map(AwardAcceptanceItem::getAwardee)
                    .filter(Objects::nonNull)
                    .findFirst().map(Arrays::asList).orElseGet(Arrays::asList);
        } else {
            return tenderProcess.getSingleAwardNotification().getAwardee();
        }
    }


    private void addSupplierInfo() {
        awardeeSelector = new Select2ChoiceBootstrapFormComponent<>("awardee",
                new GenericChoiceProvider<>(getAcceptedSupplier(editForm.getModelObject().getTenderProcess()))
        );
        awardeeSelector.setEnabled(!editForm.getModelObject().getTenderProcess().hasNonDraftImplForms());
        awardeeSelector.getField().add(new AwardeeAjaxComponentUpdatingBehavior());
        editForm.add(awardeeSelector);

        targetGroup = ComponentUtil.addSelect2ChoiceField(editForm, "targetGroup", new EntityListChoiceProvider<>(
                editForm.getModel().map(Contract::getAwardee).map(Supplier::getTargetGroups)));

        supplierAddress = new GenericSleepFormComponent<>("supplierAddress",
                editForm.getModel().map(Contract::getAwardee).map(Supplier::getAddress));
        supplierAddress.setOutputMarkupId(true);
        editForm.add(supplierAddress);

    }

    class AwardeeAjaxComponentUpdatingBehavior extends AjaxFormComponentUpdatingBehavior {
        AwardeeAjaxComponentUpdatingBehavior() {
            super("change");
        }

        @Override
        protected void onUpdate(final AjaxRequestTarget target) {
            Contract contract = editForm.getModelObject();
            if (contract.getTargetGroup() != null && contract.getAwardee() != null
                    && !contract.getAwardee().getTargetGroups().contains(contract.getTargetGroup())) {
                contract.setTargetGroup(null);
            }
            if (contract.getAwardee() != null && contract.getAwardee().getTargetGroups().size() == 1) {
                contract.setTargetGroup(contract.getAwardee().getTargetGroups().get(0));
                targetGroup.getField().clearInput();
            }
            target.add(targetGroup, supplierAddress);
        }
    }
}
