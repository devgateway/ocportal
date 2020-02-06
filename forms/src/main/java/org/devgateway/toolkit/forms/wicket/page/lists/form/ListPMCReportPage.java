package org.devgateway.toolkit.forms.wicket.page.lists.form;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditPMCReportPage;
import org.devgateway.toolkit.persistence.dao.form.PMCReport;
import org.devgateway.toolkit.persistence.service.filterstate.JpaFilterState;
import org.devgateway.toolkit.persistence.service.filterstate.form.PMCReportFilterState;
import org.devgateway.toolkit.persistence.service.form.PMCReportService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author gmutuhu
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_IMPLEMENTATION_USER)
@MountPath
public class ListPMCReportPage extends ListAbstractImplTenderProcessMakueniEntity<PMCReport> {

    @SpringBean
    protected PMCReportService service;

    public ListPMCReportPage(final PageParameters parameters) {
        super(parameters);
        this.jpaService = service;
        this.editPageClass = EditPMCReportPage.class;
    }

    @Override
    protected void onInitialize() {
        addTenderTitleColumn();
        addFileDownloadColumn();

        super.onInitialize();
    }


    @Override
    public JpaFilterState<PMCReport> newFilterState() {
        return new PMCReportFilterState();
    }
}
