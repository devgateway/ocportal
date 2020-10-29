package org.devgateway.toolkit.forms.wicket.page.edit.form;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.page.edit.roleassignable.TechAdminRoleAssignable;
import org.devgateway.toolkit.persistence.dao.form.AbstractTenderProcessMakueniEntity;
import org.devgateway.toolkit.persistence.dao.form.AdministratorReport;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess;
import org.devgateway.toolkit.persistence.service.form.AdministratorReportService;
import org.devgateway.toolkit.persistence.service.form.TenderProcessService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author mpostelnicu
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
@MountPath
public class EditAdministratorReportPage extends EditAbstractImplTenderProcessEntityPage<AdministratorReport>
        implements TechAdminRoleAssignable {

    @SpringBean
    protected AdministratorReportService administratorReportService;

    @SpringBean
    protected TenderProcessService tenderProcessService;

    public EditAdministratorReportPage(PageParameters parameters) {
        super(parameters);
        this.jpaService = administratorReportService;
    }

    public EditAdministratorReportPage() {
        this(new PageParameters());
    }

    @Override
    protected AdministratorReport newInstance() {
        final AdministratorReport ar = super.newInstance();
        ar.setTenderProcess(sessionMetadataService.getSessionTenderProcess());
        return ar;
    }

    @Override
    protected void onInitialize() {
        editForm.attachFm("administratorReportForm");
        super.onInitialize();

        ComponentUtil.addYesNoToggle(editForm, "authorizePayment", true);
        ComponentUtil.addDateField(editForm, "approvedDate");
    }

    @Override
    protected void beforeSaveEntity(final AdministratorReport report) {
        super.beforeSaveEntity(report);

        final TenderProcess tenderProcess = report.getTenderProcess();
        tenderProcess.addAdministratorReport(report);
        tenderProcessService.save(tenderProcess);
    }

    @Override
    protected void beforeDeleteEntity(final AdministratorReport report) {
        super.beforeDeleteEntity(report);

        final TenderProcess tenderProcess = report.getTenderProcess();
        tenderProcess.removeAdministratorReport(report);
        tenderProcessService.save(tenderProcess);
    }

    @Override
    protected AbstractTenderProcessMakueniEntity getNextForm() {
        return null;
    }

}
