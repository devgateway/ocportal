package org.devgateway.toolkit.forms.wicket.page.edit.form;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.components.form.FileInputBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.Select2ChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.page.edit.roleassignable.TechAdminRoleAssignable;
import org.devgateway.toolkit.forms.wicket.providers.GenericChoiceProvider;
import org.devgateway.toolkit.persistence.dao.categories.Supplier;
import org.devgateway.toolkit.persistence.dao.form.AbstractTenderProcessMakueniEntity;
import org.devgateway.toolkit.persistence.dao.form.AdministratorReport;
import org.devgateway.toolkit.persistence.dao.form.Contract;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess;
import org.devgateway.toolkit.persistence.service.form.AdministratorReportService;
import org.devgateway.toolkit.persistence.service.form.TenderProcessService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author mpostelnicu
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_IMPLEMENTATION_USER)
@MountPath
public class EditAdministratorReportPage extends EditAbstractImplTenderProcessEntity<AdministratorReport>
        implements TechAdminRoleAssignable {

    @SpringBean
    protected AdministratorReportService administratorReportService;
    private Select2ChoiceBootstrapFormComponent<Supplier> contractSelector;

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
        super.onInitialize();

        addContractor();

        ComponentUtil.addYesNoToggle(editForm, "authorizePayment", true).required();

        final FileInputBootstrapFormComponent formDocs = new FileInputBootstrapFormComponent("formDocs");
        editForm.add(formDocs);
        formDocs.required();

        ComponentUtil.addDateField(editForm, "approvedDate").required();
    }

    public static List<Supplier> getContractor(TenderProcess tenderProcess) {
        return tenderProcess.getContract().stream()
                .map(Contract::getAwardee)
                .filter(Objects::nonNull).collect(Collectors.toList());
    }

    protected void addContractor() {
        contractSelector = new Select2ChoiceBootstrapFormComponent<>(
                "contractor",
                new GenericChoiceProvider<>(getContractor(editForm.getModelObject().getTenderProcess()))
        );
        contractSelector.required();
        editForm.add(contractSelector);
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
