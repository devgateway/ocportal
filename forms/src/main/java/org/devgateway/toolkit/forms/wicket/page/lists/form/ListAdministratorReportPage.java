package org.devgateway.toolkit.forms.wicket.page.lists.form;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditAdministratorReportPage;
import org.devgateway.toolkit.persistence.dao.form.AdministratorReport;
import org.devgateway.toolkit.persistence.service.filterstate.JpaFilterState;
import org.devgateway.toolkit.persistence.service.filterstate.form.AdministratorReportFilterState;
import org.devgateway.toolkit.persistence.service.form.AdministratorReportService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author gmutuhu
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
@MountPath
public class ListAdministratorReportPage extends ListAbstractImplTenderProcessClientEntity<AdministratorReport> {

    @SpringBean
    protected AdministratorReportService administratorReportService;

    public ListAdministratorReportPage(final PageParameters parameters) {
        super(parameters);
        this.jpaService = administratorReportService;
        this.editPageClass = EditAdministratorReportPage.class;
    }

    @Override
    protected void onInitialize() {
        attachFm("administratorReportsList");

        super.onInitialize();
    }

    @Override
    protected void addColumns() {
        super.addColumns();

//        columns.add(new TextFilteredBootstrapPropertyColumn<>(
//                new Model<>((new StringResourceModel("title", ListAdministratorReportPage.this)).getString()),
//                "tenderTitle", "tenderTitle"
//        ));
        addTenderTitleColumn();
        addAuthorizePaymentColumn();
    }

    @Override
    public JpaFilterState<AdministratorReport> newFilterState() {
        return new AdministratorReportFilterState();
    }
}
