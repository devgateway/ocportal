package org.devgateway.toolkit.forms.wicket.page.lists.form;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditInspectionReportPage;
import org.devgateway.toolkit.persistence.dao.form.InspectionReport;
import org.devgateway.toolkit.persistence.service.filterstate.JpaFilterState;
import org.devgateway.toolkit.persistence.service.filterstate.form.InspectionReportFilterState;
import org.devgateway.toolkit.persistence.service.form.InspectionReportService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author mpostelnicu
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_IMPLEMENTATION_USER)
@MountPath
public class ListInspectionReportPage extends ListAbstractImplTenderProcessMakueniEntity<InspectionReport> {

    @SpringBean
    protected InspectionReportService service;

    public ListInspectionReportPage(final PageParameters parameters) {
        super(parameters);
        this.jpaService = service;
        this.editPageClass = EditInspectionReportPage.class;
    }

    @Override
    protected void onInitialize() {

//        columns.add(new TextFilteredBootstrapPropertyColumn<>(
//                new Model<>((new StringResourceModel("title", ListAdministratorReportPage.this)).getString()),
//                "tenderTitle", "tenderTitle"
//        ));
        addTenderTitleColumn();
        addFileDownloadColumn();
        addAuthorizePaymentColumn();

        super.onInitialize();
    }


    @Override
    public JpaFilterState<InspectionReport> newFilterState() {
        return new InspectionReportFilterState();
    }
}
