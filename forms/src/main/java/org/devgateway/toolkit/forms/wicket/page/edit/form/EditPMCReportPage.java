package org.devgateway.toolkit.forms.wicket.page.edit.form;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.IFormValidator;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.ocds.forms.wicket.FormSecurityUtil;
import org.devgateway.toolkit.forms.wicket.behaviors.CountyAjaxFormComponentUpdatingBehavior;
import org.devgateway.toolkit.forms.wicket.components.form.CheckBoxBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.Select2MultiChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.page.edit.panel.PMCMemberPanel;
import org.devgateway.toolkit.forms.wicket.page.edit.panel.PMCNotesPanel;
import org.devgateway.toolkit.forms.wicket.page.edit.roleassignable.PMCRoleAssignable;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.categories.Subcounty;
import org.devgateway.toolkit.persistence.dao.categories.Ward;
import org.devgateway.toolkit.persistence.dao.form.PMCReport;
import org.devgateway.toolkit.persistence.service.category.PMCStatusService;
import org.devgateway.toolkit.persistence.service.category.ProjectClosureHandoverService;
import org.devgateway.toolkit.persistence.service.category.SubcountyService;
import org.devgateway.toolkit.persistence.service.category.WardService;
import org.devgateway.toolkit.persistence.service.form.PMCReportService;
import org.devgateway.toolkit.persistence.service.form.TenderProcessService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

import static org.devgateway.toolkit.web.security.SecurityConstants.Roles.ROLE_ADMIN;
import static org.devgateway.toolkit.web.security.SecurityConstants.Roles.ROLE_PMC_VALIDATOR;

/**
 * @author mpostelnicu
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
@MountPath
public class EditPMCReportPage extends EditAbstractImplTenderProcessEntityPage<PMCReport>
        implements PMCRoleAssignable {

    @SpringBean
    protected PMCReportService service;

    @SpringBean
    protected ProjectClosureHandoverService projectClosureHandoverService;

    @SpringBean
    protected PMCStatusService pmcStatusService;

    @SpringBean
    protected TenderProcessService tenderProcessService;

    @SpringBean
    protected SubcountyService subcountyService;

    @SpringBean
    protected WardService wardService;

    private Select2MultiChoiceBootstrapFormComponent<Ward> wards;
    private Select2MultiChoiceBootstrapFormComponent<Subcounty> subcounties;
    private CheckBoxBootstrapFormComponent acknowledgeSignature;

    public EditPMCReportPage(PageParameters parameters) {
        super(parameters);
        this.jpaService = service;
    }

    public EditPMCReportPage() {
        this(new PageParameters());
    }

    @Override
    protected PMCReport newInstance() {
        final PMCReport ar = super.newInstance();
        ar.setTenderProcess(sessionMetadataService.getSessionTenderProcess());
        return ar;
    }

    protected class PMCReportValidator implements IFormValidator {
        @Override
        public FormComponent<?>[] getDependentFormComponents() {
            return new FormComponent[0];
        }

        @Override
        public void validate(Form<?> form) {
            if (!BooleanUtils.isTrue(editForm.getModelObject().getAcknowledgeSignature())) {
                form.error(getString("mustAcknowledge"));
            }
        }
    }

    @Override
    public boolean isDisableEditingEvent() {
        return super.isDisableEditingEvent() || (FormSecurityUtil.hasUserRole(ROLE_PMC_VALIDATOR)
                && !FormSecurityUtil.hasUserRole(ROLE_ADMIN));
    }

    @Override
    protected void onInitialize() {
        editForm.attachFm("pmcReportForm");
        super.onInitialize();

        editForm.add(new PMCReportValidator());

        ComponentUtil.addYesNoToggle(editForm, "authorizePayment", true);

        ComponentUtil.addDateField(editForm, "approvedDate");

        PMCMemberPanel pmcMembers = new PMCMemberPanel("pmcMembers");
        editForm.add(pmcMembers);

        PMCNotesPanel pmcNotes = new PMCNotesPanel("pmcNotes");
        editForm.add(pmcNotes);

        ComponentUtil.addTextField(editForm, "signatureNames");
        acknowledgeSignature = ComponentUtil.addCheckBox(editForm, "acknowledgeSignature");
        editForm.add(acknowledgeSignature);

        ComponentUtil.addTextAreaField(editForm, "socialSafeguards");
        ComponentUtil.addTextAreaField(editForm, "emergingComplaints");
        ComponentUtil.addTextAreaField(editForm, "pmcChallenges");

        ComponentUtil.addSelect2ChoiceField(editForm, "pmcStatus", pmcStatusService);
        ComponentUtil.addSelect2MultiChoiceField(editForm, "projectClosureHandover", projectClosureHandoverService);
        wards = ComponentUtil.addSelect2MultiChoiceField(editForm, "wards", wardService);

        subcounties = ComponentUtil.addSelect2MultiChoiceField(editForm, "subcounties", subcountyService);
        subcounties.getField().add(new CountyAjaxFormComponentUpdatingBehavior<>(subcounties, wards,
                LoadableDetachableModel.of(() -> wardService), editForm.getModelObject()::setWards, "change"
        ));
       
        formDocs.getField().setRequireAtLeastOneItem(false);
    }

    @Override
    protected void addSaveButtonsPermissions(final Component button) {
        addDefaultAllButtonsPermissions(button);
        button.setVisibilityAllowed(button.isVisibilityAllowed()
                && DBConstants.Status.DRAFT.equals(editForm.getModelObject().getStatus()));
    }

    @Override
    protected void onBeforeRender() {
        super.onBeforeRender();
        deleteButton.setEnabled(true);
    }

    @Override
    protected void onBeforeRevertToDraft(AjaxRequestTarget target) {
        editForm.getModelObject().setRejected(true);
    }

    @Override
    protected void onBeforeSaveApprove(AjaxRequestTarget target) {
        editForm.getModelObject().setRejected(false);
    }

    @Override
    protected void onApproved() {
        super.onApproved();
        service.onApproved(editForm.getModelObject());
    }
}
