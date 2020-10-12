package org.devgateway.toolkit.forms.wicket.page.edit.form;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.behaviors.CountyAjaxFormComponentUpdatingBehavior;
import org.devgateway.toolkit.forms.wicket.components.form.Select2MultiChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.page.edit.panel.PMCMemberPanel;
import org.devgateway.toolkit.forms.wicket.page.edit.roleassignable.PMCRoleAssignable;
import org.devgateway.toolkit.persistence.dao.categories.Subcounty;
import org.devgateway.toolkit.persistence.dao.categories.Ward;
import org.devgateway.toolkit.persistence.dao.form.AbstractTenderProcessMakueniEntity;
import org.devgateway.toolkit.persistence.dao.form.PMCReport;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess;
import org.devgateway.toolkit.persistence.service.category.PMCStatusService;
import org.devgateway.toolkit.persistence.service.category.ProjectClosureHandoverService;
import org.devgateway.toolkit.persistence.service.category.SubcountyService;
import org.devgateway.toolkit.persistence.service.category.WardService;
import org.devgateway.toolkit.persistence.service.form.PMCReportService;
import org.devgateway.toolkit.persistence.service.form.TenderProcessService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

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

    @Override
    protected void onInitialize() {
        super.onInitialize();

        ComponentUtil.addYesNoToggle(editForm, "authorizePayment", true).required();

        ComponentUtil.addDateField(editForm, "approvedDate").required();

        PMCMemberPanel pmcMembers = new PMCMemberPanel("pmcMembers");
        editForm.add(pmcMembers);

        ComponentUtil.addSelect2ChoiceField(editForm, "pmcStatus", pmcStatusService).required();
        ComponentUtil.addSelect2MultiChoiceField(editForm, "projectClosureHandover", projectClosureHandoverService)
                .required();

        wards = ComponentUtil.addSelect2MultiChoiceField(editForm, "wards", wardService);
        wards.required();

        subcounties = ComponentUtil.addSelect2MultiChoiceField(editForm, "subcounties", subcountyService);
        subcounties.getField().add(new CountyAjaxFormComponentUpdatingBehavior<>(subcounties, wards,
                LoadableDetachableModel.of(() -> wardService), editForm.getModelObject()::setWards, "change"
        ));
        subcounties.required();

        formDocs.maxFiles(1);
    }

    @Override
    protected void beforeSaveEntity(final PMCReport report) {
        super.beforeSaveEntity(report);

        final TenderProcess tenderProcess = report.getTenderProcess();
        tenderProcess.addPMCReport(report);
        tenderProcessService.save(tenderProcess);
    }

    @Override
    protected void beforeDeleteEntity(final PMCReport report) {
        super.beforeDeleteEntity(report);

        final TenderProcess tenderProcess = report.getTenderProcess();
        tenderProcess.removePMCReport(report);
        tenderProcessService.save(tenderProcess);
    }

    @Override
    protected AbstractTenderProcessMakueniEntity getNextForm() {
        return null;
    }

    @Override
    protected void onApproved() {
        super.onApproved();
        service.onApproved(editForm.getModelObject());
    }
}
