package org.devgateway.toolkit.forms.wicket.page.edit.form;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.wicket.components.form.FileInputBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.GenericSleepFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.events.EditingDisabledEvent;
import org.devgateway.toolkit.forms.wicket.page.BasePage;
import org.devgateway.toolkit.forms.wicket.page.edit.panel.PurchaseItemPanel;
import org.devgateway.toolkit.forms.wicket.page.overview.status.StatusOverviewPage;
import org.devgateway.toolkit.persistence.dao.form.Project;
import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisition;
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

    @Override
    protected void checkAndSendEventForDisableEditing() {
        super.checkAndSendEventForDisableEditing();
        if (isTerminated()) {
            send(getPage(), Broadcast.BREADTH, new EditingDisabledEvent());
        }
    }

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

        editForm.add(new GenericSleepFormComponent<>("project.procurementPlan.department"));
        editForm.add(new GenericSleepFormComponent<>("project.procurementPlan.fiscalYear"));

        if (isTerminated()) {
            alertTerminated.setVisibilityAllowed(true);
        }

        final GenericSleepFormComponent purchaseRequestNumber =
                new GenericSleepFormComponent<>("purchaseRequestNumber");
        editForm.add(purchaseRequestNumber);
        if (entityId == null) {
            purchaseRequestNumber.setVisibilityAllowed(false);
        } else {
            purchaseRequestNumber.setVisibilityAllowed(true);
        }

        ComponentUtil.addSelect2ChoiceField(editForm, "requestedBy", staffService).required();
        ComponentUtil.addSelect2ChoiceField(editForm, "chargeAccount", chargeAccountService).required();
        ComponentUtil.addDateField(editForm, "requestApprovalDate").required();

        editForm.add(new PurchaseItemPanel("purchaseItems"));

        ComponentUtil.addDateField(editForm, "approvedDate").required();

        final FileInputBootstrapFormComponent formDocs = new FileInputBootstrapFormComponent("formDocs");
        formDocs.required();
        editForm.add(formDocs);
        saveTerminateButton.setVisibilityAllowed(false);
    }

    @Override
    protected PurchaseRequisition newInstance() {
        final PurchaseRequisition purchaseRequisition = super.newInstance();
        purchaseRequisition.setProject(sessionMetadataService.getSessionProject());

        return purchaseRequisition;
    }

    @Override
    public boolean isTerminated() {
        final PurchaseRequisition purchaseRequisition = editForm.getModelObject();
        return purchaseRequisition.isTerminated();
    }

    @Override
    protected void beforeSaveEntity(final PurchaseRequisition purchaseRequisition) {
        super.beforeSaveEntity(purchaseRequisition);

        final Project project = purchaseRequisition.getProject();
        project.addPurchaseRequisition(purchaseRequisition);
        projectService.save(project);
    }

    @Override
    protected void afterSaveEntity(final PurchaseRequisition saveable) {
        super.afterSaveEntity(saveable);

        // autogenerate the number
        if (saveable.getPurchaseRequestNumber() == null) {
            saveable.setPurchaseRequestNumber(saveable.getProcurementPlan().getDepartment().getCode()
                    + "/" + saveable.getId());

            jpaService.saveAndFlush(saveable);
        }

        // add current Purchase Requisition in session
        sessionMetadataService.setSessionPurchaseRequisition(editForm.getModelObject());
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
        // add current Purchase Requisition in session
        sessionMetadataService.setSessionPurchaseRequisition(editForm.getModelObject());

        return pp;
    }
}
