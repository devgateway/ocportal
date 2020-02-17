package org.devgateway.toolkit.forms.wicket.page.edit.form;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.components.form.GenericSleepFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.page.edit.roleassignable.MEPaymentRoleAssignable;
import org.devgateway.toolkit.forms.wicket.providers.GenericChoiceProvider;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.form.AbstractImplTenderProcessMakueniEntity;
import org.devgateway.toolkit.persistence.dao.form.AbstractTenderProcessMakueniEntity;
import org.devgateway.toolkit.persistence.dao.form.PaymentVoucher;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess;
import org.devgateway.toolkit.persistence.service.form.AbstractMakueniEntityService;
import org.devgateway.toolkit.persistence.service.form.AdministratorReportService;
import org.devgateway.toolkit.persistence.service.form.InspectionReportService;
import org.devgateway.toolkit.persistence.service.form.PMCReportService;
import org.devgateway.toolkit.persistence.service.form.PaymentVoucherService;
import org.devgateway.toolkit.persistence.service.form.TenderProcessService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.stream.Collectors;

/**
 * @author mpostelnicu
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
@MountPath
public class EditPaymentVoucherPage extends EditAbstractImplTenderProcessEntityPage<PaymentVoucher>
        implements MEPaymentRoleAssignable {

    @SpringBean
    protected PaymentVoucherService service;

    @SpringBean
    protected PMCReportService pmcReportService;

    @SpringBean
    protected InspectionReportService inspectionReportService;

    @SpringBean
    protected AdministratorReportService administratorReportService;

    @SpringBean
    protected TenderProcessService tenderProcessService;

    public EditPaymentVoucherPage(PageParameters parameters) {
        super(parameters);
        this.jpaService = service;
    }

    public EditPaymentVoucherPage() {
        this(new PageParameters());
    }

    @Override
    protected PaymentVoucher newInstance() {
        final PaymentVoucher ar = super.newInstance();
        ar.setTenderProcess(sessionMetadataService.getSessionTenderProcess());
        return ar;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        Fragment inspectionExtraFields = new Fragment("childExtraFields", "paymentExtraFields", this);
        abstractImplExtraFields.replace(inspectionExtraFields);
        inspectionExtraFields.add(new GenericSleepFormComponent<>("tenderProcess.singleContract.referenceNumber"));


        ComponentUtil.addBigDecimalField(editForm, "totalAmount").required();


        ComponentUtil.addSelect2ChoiceField(editForm, "pmcReport",
                submittedAndWithinTenderProcessProvider(pmcReportService)
        ).required();
        ComponentUtil.addSelect2ChoiceField(editForm, "inspectionReport",
                submittedAndWithinTenderProcessProvider(inspectionReportService)
        ).required();
        ComponentUtil.addSelect2ChoiceField(editForm, "administratorReport",
                submittedAndWithinTenderProcessProvider(administratorReportService)
        ).required();
        ComponentUtil.addYesNoToggle(editForm, "lastPayment", true).required();
        ComponentUtil.addDateField(editForm, "approvedDate").required();

        formDocs.maxFiles(1);
    }

    private <X extends AbstractImplTenderProcessMakueniEntity> GenericChoiceProvider<X>
    submittedAndWithinTenderProcessProvider(AbstractMakueniEntityService<X> service) {
        return new GenericChoiceProvider<>(service.findAll().stream().filter(x -> x.getContract().equals(
                editForm.getModelObject().getTenderProcess().getSingleContract())
                && (x.getStatus().equals(DBConstants.Status.SUBMITTED) || x.getStatus()
                .equals(DBConstants.Status.APPROVED))).collect(Collectors.toList()));
    }

    @Override
    protected void beforeSaveEntity(final PaymentVoucher report) {
        super.beforeSaveEntity(report);

        final TenderProcess tenderProcess = report.getTenderProcess();
        tenderProcess.addPaymentVoucher(report);
        tenderProcessService.save(tenderProcess);
    }

    @Override
    protected void beforeDeleteEntity(final PaymentVoucher report) {
        super.beforeDeleteEntity(report);

        final TenderProcess tenderProcess = report.getTenderProcess();
        tenderProcess.removePaymentVoucher(report);
        tenderProcessService.save(tenderProcess);
    }

    @Override
    protected AbstractTenderProcessMakueniEntity getNextForm() {
        return null;
    }

}
