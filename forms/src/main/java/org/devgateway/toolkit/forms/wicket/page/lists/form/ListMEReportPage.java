package org.devgateway.toolkit.forms.wicket.page.lists.form;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.components.table.SelectFilteredBootstrapPropertyColumn;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditMEReportPage;
import org.devgateway.toolkit.persistence.dao.form.MEReport;
import org.devgateway.toolkit.persistence.service.category.MEStatusService;
import org.devgateway.toolkit.persistence.service.filterstate.JpaFilterState;
import org.devgateway.toolkit.persistence.service.filterstate.form.MEReportFilterState;
import org.devgateway.toolkit.persistence.service.form.MEReportService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author gmutuhu
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_IMPLEMENTATION_USER)
@MountPath
public class ListMEReportPage extends ListAbstractImplTenderProcessMakueniEntity<MEReport> {

    @SpringBean
    protected MEReportService service;

    @SpringBean
    protected MEStatusService meStatusService;

    public ListMEReportPage(final PageParameters parameters) {
        super(parameters);
        this.jpaService = service;
        this.editPageClass = EditMEReportPage.class;
    }

    @Override
    protected void onInitialize() {
        addTenderTitleColumn();
        addFileDownloadColumn();
        addMEStatusColumn();

        super.onInitialize();
    }

    protected void addMEStatusColumn() {
        columns.add(new SelectFilteredBootstrapPropertyColumn<>(
                new Model<>(
                        (new StringResourceModel("meStatus", ListMEReportPage.this)).getString()),
                "meStatus",
                "meStatus",
                LoadableDetachableModel.of(() -> meStatusService.findAll()), dataTable, false
        ));
    }


    @Override
    public JpaFilterState<MEReport> newFilterState() {
        return new MEReportFilterState();
    }
}
