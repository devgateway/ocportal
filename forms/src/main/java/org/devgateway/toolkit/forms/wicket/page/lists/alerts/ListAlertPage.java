package org.devgateway.toolkit.forms.wicket.page.lists.alerts;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.page.edit.alerts.EditAlertPage;
import org.devgateway.toolkit.forms.wicket.page.lists.AbstractListPage;
import org.devgateway.toolkit.persistence.dao.alerts.Alert;
import org.devgateway.toolkit.persistence.service.alerts.AlertService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author idobre
 * @since 30/08/2019
 */

@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
@MountPath(value = "/alerts")
public class ListAlertPage extends AbstractListPage<Alert> {
    @SpringBean
    private AlertService alertService;

    public ListAlertPage(final PageParameters parameters) {
        super(parameters);

        this.jpaService = alertService;
        this.editPageClass = EditAlertPage.class;
    }

    @Override
    protected void onInitialize() {


        super.onInitialize();
    }
}
