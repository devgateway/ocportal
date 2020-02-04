package org.devgateway.toolkit.forms.wicket.page.edit.form;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.behaviors.CountyAjaxFormComponentUpdatingBehavior;
import org.devgateway.toolkit.forms.wicket.components.form.FileInputBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.GenericSleepFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.Select2MultiChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.page.edit.roleassignable.MEPaymentRoleAssignable;
import org.devgateway.toolkit.persistence.dao.categories.Subcounty;
import org.devgateway.toolkit.persistence.dao.categories.Ward;
import org.devgateway.toolkit.persistence.dao.form.AbstractTenderProcessMakueniEntity;
import org.devgateway.toolkit.persistence.dao.form.MEReport;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess;
import org.devgateway.toolkit.persistence.service.category.MEStaffService;
import org.devgateway.toolkit.persistence.service.category.MEStatusService;
import org.devgateway.toolkit.persistence.service.category.SubcountyService;
import org.devgateway.toolkit.persistence.service.category.WardService;
import org.devgateway.toolkit.persistence.service.form.MEReportService;
import org.devgateway.toolkit.persistence.service.form.TenderProcessService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author mpostelnicu
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_IMPLEMENTATION_USER)
@MountPath
public class EditMEReportPage extends EditAbstractImplTenderProcessEntity<MEReport>
        implements MEPaymentRoleAssignable {

    @SpringBean
    protected MEReportService service;

    @SpringBean
    protected MEStaffService meStaffService;

    @SpringBean
    protected MEStatusService meStatusService;

    @SpringBean
    protected TenderProcessService tenderProcessService;

    @SpringBean
    protected SubcountyService subcountyService;

    @SpringBean
    protected WardService wardService;

    private Select2MultiChoiceBootstrapFormComponent<Ward> wards;
    private Select2MultiChoiceBootstrapFormComponent<Subcounty> subcounties;

    public EditMEReportPage(PageParameters parameters) {
        super(parameters);
        this.jpaService = service;
    }

    public EditMEReportPage() {
        this(new PageParameters());
    }

    @Override
    protected MEReport newInstance() {
        final MEReport ar = super.newInstance();
        ar.setTenderProcess(sessionMetadataService.getSessionTenderProcess());
        return ar;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        Fragment inspectionExtraFields = new Fragment("childExtraFields", "meExtraFields", this);
        abstractImplExtraFields.replace(inspectionExtraFields);
        inspectionExtraFields.add(new GenericSleepFormComponent<>("tenderProcess.singleContract.contractValue"));

        ComponentUtil.addIntegerTextField(editForm, "sno").required();
        ComponentUtil.addBigDecimalField(editForm, "lpoAmount").required();
        ComponentUtil.addTextField(editForm, "lpoNumber").required();
        ComponentUtil.addBigDecimalField(editForm, "expenditure").required();
        ComponentUtil.addBigDecimalField(editForm, "uncommitted").required();
        ComponentUtil.addTextAreaField(editForm, "projectScope").required();
        ComponentUtil.addTextAreaField(editForm, "output").required();
        ComponentUtil.addTextAreaField(editForm, "outcome").required();
        ComponentUtil.addTextAreaField(editForm, "projectProgress").required();
        ComponentUtil.addIntegerTextField(editForm, "directBeneficiariesTarget").required();
        ComponentUtil.addTextAreaField(editForm, "wayForward").required();
        ComponentUtil.addDateField(editForm, "byWhen").required();
        ComponentUtil.addYesNoToggle(editForm, "inspected", true).required();
        ComponentUtil.addYesNoToggle(editForm, "invoiced", true).required();
        ComponentUtil.addSelect2ChoiceField(editForm, "officerResponsible", meStaffService).required();
        ComponentUtil.addSelect2ChoiceField(editForm, "meStatus", meStatusService).required();
        ComponentUtil.addTextAreaField(editForm, "remarks").required();
        ComponentUtil.addTextField(editForm, "contractorContact").required();

        final FileInputBootstrapFormComponent formDocs = new FileInputBootstrapFormComponent("formDocs");
        editForm.add(formDocs);
        formDocs.required();

        ComponentUtil.addDateField(editForm, "approvedDate").required();

        wards = ComponentUtil.addSelect2MultiChoiceField(editForm, "wards", wardService);
        wards.required();

        subcounties = ComponentUtil.addSelect2MultiChoiceField(editForm, "subcounties", subcountyService);
        subcounties.getField()
                .add(new CountyAjaxFormComponentUpdatingBehavior(subcounties, wards, wardService, editForm.getModel(),
                        "change"
                ));
        subcounties.required();
    }

    @Override
    protected void beforeSaveEntity(final MEReport report) {
        super.beforeSaveEntity(report);

        final TenderProcess tenderProcess = report.getTenderProcess();
        tenderProcess.addMEReport(report);
        tenderProcessService.save(tenderProcess);
    }

    @Override
    protected void beforeDeleteEntity(final MEReport report) {
        super.beforeDeleteEntity(report);

        final TenderProcess tenderProcess = report.getTenderProcess();
        tenderProcess.removeMEReport(report);
        tenderProcessService.save(tenderProcess);
    }

    @Override
    protected AbstractTenderProcessMakueniEntity getNextForm() {
        return null;
    }

}
