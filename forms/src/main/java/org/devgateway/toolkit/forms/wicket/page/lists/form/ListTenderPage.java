package org.devgateway.toolkit.forms.wicket.page.lists.form;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.components.table.filter.JpaFilterState;
import org.devgateway.toolkit.forms.wicket.components.table.filter.form.TenderFilterState;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditTenderPage;
import org.devgateway.toolkit.persistence.dao.form.Tender;
import org.devgateway.toolkit.persistence.service.form.TenderService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author gmutuhu
 *
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
@MountPath("/tenders")
public class ListTenderPage extends ListAbstractMakueniEntityPage<Tender> {
    
    @SpringBean
    protected TenderService tenderService;

    public ListTenderPage(final PageParameters parameters) {
        super(parameters);
        this.jpaService = tenderService;
        this.editPageClass = EditTenderPage.class;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
    }

    @Override
    public JpaFilterState<Tender> newFilterState() {
        return new TenderFilterState();
    }
}
