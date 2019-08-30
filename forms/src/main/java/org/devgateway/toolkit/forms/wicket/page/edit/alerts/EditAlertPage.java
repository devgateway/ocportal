package org.devgateway.toolkit.forms.wicket.page.edit.alerts;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.page.edit.AbstractEditPage;
import org.devgateway.toolkit.forms.wicket.page.lists.alerts.ListAlertPage;
import org.devgateway.toolkit.persistence.dao.alerts.Alert;
import org.devgateway.toolkit.persistence.service.alerts.AlertService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author idobre
 * @since 30/08/2019
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
@MountPath("/alert")
public class EditAlertPage extends AbstractEditPage<Alert> {
    @SpringBean
    private AlertService alertService;

    public EditAlertPage(final PageParameters parameters) {
        super(parameters);

        jpaService = alertService;
        listPageClass = ListAlertPage.class;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

    }
}
