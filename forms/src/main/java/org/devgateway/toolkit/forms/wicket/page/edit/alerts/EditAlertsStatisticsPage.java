package org.devgateway.toolkit.forms.wicket.page.edit.alerts;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.page.edit.AbstractEditPage;
import org.devgateway.toolkit.forms.wicket.page.lists.alerts.ListAlertsStatisticsPage;
import org.devgateway.toolkit.persistence.dao.alerts.AlertsStatistics;
import org.devgateway.toolkit.persistence.service.alerts.AlertsStatisticsService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author idobre
 * @since 30/08/2019
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
@MountPath("/alertStatistics")
public class EditAlertsStatisticsPage extends AbstractEditPage<AlertsStatistics> {
    @SpringBean
    private AlertsStatisticsService alertsStatisticsService;

    public EditAlertsStatisticsPage(final PageParameters parameters) {
        super(parameters);

        jpaService = alertsStatisticsService;
        listPageClass = ListAlertsStatisticsPage.class;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

    }
}
