package org.devgateway.toolkit.forms.wicket.page.edit.form;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.wicket.components.form.GenericSleepFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.events.EditingDisabledEvent;
import org.devgateway.toolkit.forms.wicket.page.BasePage;
import org.devgateway.toolkit.forms.wicket.page.edit.panel.PurchRequisitionPanel;
import org.devgateway.toolkit.forms.wicket.page.overview.status.StatusOverviewPage;
import org.devgateway.toolkit.persistence.dao.form.Project;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess;
import org.devgateway.toolkit.persistence.service.category.ChargeAccountService;
import org.devgateway.toolkit.persistence.service.category.StaffService;
import org.devgateway.toolkit.persistence.service.form.ProjectService;
import org.devgateway.toolkit.persistence.service.form.TenderProcessService;
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
public class EditTenderProcessPage extends EditAbstractMakueniEntityPage<TenderProcess> {
    @SpringBean
    private TenderProcessService tenderProcessService;

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

    public EditTenderProcessPage(final PageParameters parameters) {
        super(parameters);
        this.jpaService = tenderProcessService;

        // check if this is a new object and redirect user to dashboard page if we don't have all the needed info
        if (entityId == null && sessionMetadataService.getSessionProject() == null) {
            logger.warn("Something wrong happened since we are trying to add a new TenderProcess Entity "
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

//        ComponentUtil.addSelect2ChoiceField(editForm, "requestedBy", staffService).required();
//        ComponentUtil.addSelect2ChoiceField(editForm, "chargeAccount", chargeAccountService).required();
//        ComponentUtil.addDateField(editForm, "requestApprovalDate").required();

//        editForm.add(new PurchaseItemPanel("purchaseItems"));

        editForm.add(new PurchRequisitionPanel("purchRequisitions"));

        ComponentUtil.addDateField(editForm, "approvedDate").required();

        saveTerminateButton.setVisibilityAllowed(false);
    }

    @Override
    protected TenderProcess newInstance() {
        final TenderProcess tenderProcess = super.newInstance();
        tenderProcess.setProject(sessionMetadataService.getSessionProject());

        return tenderProcess;
    }

    @Override
    public boolean isTerminated() {
        final TenderProcess tenderProcess = editForm.getModelObject();
        return tenderProcess.isTerminated();
    }

    @Override
    protected void beforeSaveEntity(final TenderProcess tenderProcess) {
        super.beforeSaveEntity(tenderProcess);

        final Project project = tenderProcess.getProject();
        project.addTenderProcess(tenderProcess);
        projectService.save(project);
    }

    @Override
    protected void afterSaveEntity(final TenderProcess saveable) {
        super.afterSaveEntity(saveable);

        // autogenerate the number
        if (saveable.getPurchaseRequestNumber() == null) {
            saveable.setPurchaseRequestNumber(saveable.getProcurementPlan().getDepartment().getCode()
                    + "/" + saveable.getId());

            jpaService.saveAndFlush(saveable);
        }

        // add current Purchase Requisition in session
        sessionMetadataService.setSessionTenderProcess(editForm.getModelObject());
    }

    @Override
    protected void beforeDeleteEntity(final TenderProcess tenderProcess) {
        super.beforeDeleteEntity(tenderProcess);

        final Project project = tenderProcess.getProject();
        project.remoteTenderProcess(tenderProcess);
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
        sessionMetadataService.setSessionTenderProcess(editForm.getModelObject());

        return pp;
    }
}
