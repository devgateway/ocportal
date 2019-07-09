package org.devgateway.toolkit.forms.wicket.page.edit.form;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.validators.UniquePropertyEntryValidator;
import org.devgateway.toolkit.forms.wicket.components.form.FileInputBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.page.BasePage;
import org.devgateway.toolkit.forms.wicket.page.edit.panel.PurchaseItemPanel;
import org.devgateway.toolkit.forms.wicket.page.overview.status.StatusOverviewPage;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.dao.form.Project;
import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisition;
import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisition_;
import org.devgateway.toolkit.persistence.service.category.ChargeAccountService;
import org.devgateway.toolkit.persistence.service.category.StaffService;
import org.devgateway.toolkit.persistence.service.form.ProjectService;
import org.devgateway.toolkit.persistence.service.form.PurchaseRequisitionService;
import org.devgateway.toolkit.persistence.spring.PersistenceUtil;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.springframework.util.ObjectUtils;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author idobre
 * @since 2019-04-17
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
@MountPath("/purchaseRequisition")
public class EditPurchaseRequisitionPage extends EditAbstractMakueniEntityPage<PurchaseRequisition> {
    @SpringBean
    private PurchaseRequisitionService purchaseRequisitionService;

    @SpringBean
    private ProjectService projectService;

    @SpringBean
    protected StaffService staffService;

    @SpringBean
    protected ChargeAccountService chargeAccountService;


    public EditPurchaseRequisitionPage(final PageParameters parameters) {
        super(parameters);
        this.jpaService = purchaseRequisitionService;

        // check if this is a new object and redirect user to dashboard page if we don't have all the needed info
        if (entityId == null && sessionMetadataService.getSessionProject() == null) {
            logger.warn("Something wrong happened since we are trying to add a new PurchaseRequisition Entity "
                    + "without having a Project!");
            setResponsePage(StatusOverviewPage.class);
        }
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        final TextFieldBootstrapFormComponent<String> title = ComponentUtil.addTextField(editForm, "title");
        title.required();
        title.getField().add(WebConstants.StringValidators.MAXIMUM_LENGTH_VALIDATOR_STD_DEFAULT_TEXT);
        title.getField().add(uniqueTitle());

        final TextFieldBootstrapFormComponent<String> purchaseRequestNumber =
                ComponentUtil.addTextField(editForm, "purchaseRequestNumber");
        purchaseRequestNumber.required();
        purchaseRequestNumber.getField().add(WebConstants.StringValidators.MAXIMUM_LENGTH_VALIDATOR_STD_DEFAULT_TEXT);
        purchaseRequestNumber.getField().add(new UniquePropertyEntryValidator<>(
                getString("uniqueNumber"),
                purchaseRequisitionService::findOne,
                (o, v) -> (root, query, cb)
                        -> cb.equal(cb.lower(root.get(PurchaseRequisition_.purchaseRequestNumber)), v.toLowerCase()),
                editForm.getModel()));

        ComponentUtil.addSelect2ChoiceField(editForm, "requestedBy", staffService).required();
        ComponentUtil.addSelect2ChoiceField(editForm, "chargeAccount", chargeAccountService).required();
        ComponentUtil.addDateField(editForm, "requestApprovalDate").required();

        editForm.add(new PurchaseItemPanel("purchaseItems"));

        ComponentUtil.addDateField(editForm, "approvedDate").required();

        final FileInputBootstrapFormComponent formDocs = new FileInputBootstrapFormComponent("formDocs");
        formDocs.required();
        editForm.add(formDocs);
        saveTerminateButton.setVisibilityAllowed(false);

//        Release release = ocdsConversionService.createRelease(editForm.getModelObject());
//        try {
//            System.out.println(mapper.writeValueAsString(release));
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    protected PurchaseRequisition newInstance() {
        final PurchaseRequisition purchaseRequisition = super.newInstance();
        purchaseRequisition.setProject(sessionMetadataService.getSessionProject());

        return purchaseRequisition;
    }

    @Override
    protected void beforeSaveEntity(final PurchaseRequisition purchaseRequisition) {
        super.beforeSaveEntity(purchaseRequisition);

        final Project project = purchaseRequisition.getProject();
        project.addPurchaseRequisition(purchaseRequisition);
        projectService.save(project);
    }

    @Override
    protected void beforeDeleteEntity(final PurchaseRequisition purchaseRequisition) {
        super.beforeDeleteEntity(purchaseRequisition);

        final Project project = purchaseRequisition.getProject();
        project.removePurchaseRequisition(purchaseRequisition);
        projectService.save(project);
    }

    @Override
    protected Class<? extends BasePage> pageAfterSubmitAndNext() {
        return EditTenderPage.class;
    }

    @Override
    protected PageParameters parametersAfterSubmitAndNext() {
        final PageParameters pp = new PageParameters();
        if (!ObjectUtils.isEmpty(editForm.getModelObject().getTender())) {
            pp.set(WebConstants.PARAM_ID, PersistenceUtil.getNext(editForm.getModelObject().getTender()).getId());
        }
        // check if we have a Purchase Requisition in session and add it
        if (sessionMetadataService.getSessionPurchaseRequisition() == null) {
            sessionMetadataService.setSessionPurchaseRequisition(editForm.getModelObject());
        }

        return pp;
    }

    private IValidator<String> uniqueTitle() {
        final StringValue id = getPageParameters().get(WebConstants.PARAM_ID);
        return new UniqueTitleValidator(id.toLong(-1));
    }

    public class UniqueTitleValidator implements IValidator<String> {
        private final Long id;

        public UniqueTitleValidator(final Long id) {
            this.id = id;
        }

        @Override
        public void validate(final IValidatable<String> validatable) {
            final String titleValue = validatable.getValue();
            final Project project = editForm.getModelObject().getProject();

            if (project != null && titleValue != null) {
                ProcurementPlan procurementPlan = project.getProcurementPlan();
                if (purchaseRequisitionService
                        .countByProjectProcurementPlanAndTitleAndIdNot(procurementPlan, titleValue, id) > 0) {
                    final ValidationError error = new ValidationError(getString("uniqueTitle"));
                    validatable.error(error);
                }
            }
        }
    }
}
