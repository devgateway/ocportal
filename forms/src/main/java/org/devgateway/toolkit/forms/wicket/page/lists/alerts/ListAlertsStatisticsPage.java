package org.devgateway.toolkit.forms.wicket.page.lists.alerts;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.components.table.SimpleDateProperyColumn;
import org.devgateway.toolkit.forms.wicket.page.edit.alerts.EditAlertsStatisticsPage;
import org.devgateway.toolkit.forms.wicket.page.lists.AbstractListPage;
import org.devgateway.toolkit.persistence.dao.alerts.AlertsStatistics;
import org.devgateway.toolkit.persistence.service.alerts.AlertsStatisticsService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

import java.time.format.DateTimeFormatter;

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

        columns.add(new SimpleDateProperyColumn<>(new Model<>("Sending Date"),
                "createdDate", "createdDate",
                t -> t.getLastModifiedDate().orElse(null), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        columns.add(new PropertyColumn<>(new Model<>("# Alerts Send"), "numberSentAlerts", "numberSentAlerts"));
        columns.add(new PropertyColumn<>(new Model<>("# Errors"), "numberErrors", "numberErrors"));
        columns.add(new PropertyColumn<>(new Model<>("DB Time in Sec"), "dbTime", "dbTime"));
        columns.add(new PropertyColumn<>(new Model<>("Sending Time in Sec"), "sendingTime", "sendingTime"));

        super.onInitialize();

        columns.remove(6);
    }
}
