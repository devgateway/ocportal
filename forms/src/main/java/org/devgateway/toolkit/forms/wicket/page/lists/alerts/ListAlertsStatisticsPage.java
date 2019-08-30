package org.devgateway.toolkit.forms.wicket.page.lists.alerts;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.page.edit.alerts.EditAlertsStatisticsPage;
import org.devgateway.toolkit.forms.wicket.page.lists.AbstractListPage;
import org.devgateway.toolkit.persistence.dao.alerts.AlertsStatistics;
import org.devgateway.toolkit.persistence.service.alerts.AlertsStatisticsService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

/**
 * @author idobre
 * @since 30/08/2019
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
@MountPath(value = "/alertsStatistics")
public class ListAlertsStatisticsPage extends AbstractListPage<AlertsStatistics> {
    @SpringBean
    private AlertsStatisticsService alertsStatisticsService;

    public ListAlertsStatisticsPage(final PageParameters parameters) {
        super(parameters);

        this.jpaService = alertsStatisticsService;
        this.editPageClass = EditAlertsStatisticsPage.class;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        columns.remove(1);


    }
}
