package org.devgateway.toolkit.forms.wicket.page.lists.flags;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.ocds.web.spring.ReleaseFlagNotificationService;
import org.devgateway.toolkit.forms.wicket.page.lists.AbstractListPage;
import org.devgateway.toolkit.persistence.dao.flags.ReleaseFlagHistory;
import org.devgateway.toolkit.persistence.service.ReleaseFlagHistoryService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author mpostelnicu
 */
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
@MountPath(value = "/redFlagHistory")
public class ListFlagHistoryPage extends AbstractListPage<ReleaseFlagHistory> {
    @SpringBean
    private ReleaseFlagHistoryService releaseFlagHistoryService;

    @SpringBean
    private ReleaseFlagNotificationService releaseFlagNotificationService;

    public ListFlagHistoryPage(final PageParameters parameters) {
        super(parameters);

        this.jpaService = releaseFlagHistoryService;
        hasEditPage = false;
        hasNewPage = false;
    }

    @Override
    protected void onInitialize() {
        columns.add(new PropertyColumn<ReleaseFlagHistory, String>(new Model<>("Flagged Date"),
                "flaggedDate", "flaggedDate") {
            @Override
            public void populateItem(final Item<ICellPopulator<ReleaseFlagHistory>> item,
                                     final String componentId,
                                     final IModel<ReleaseFlagHistory> rowModel) {
                final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                ZonedDateTime flaggedDate = rowModel.getObject().getFlaggedDate();
                item.add(new Label(componentId, flaggedDate.format(formatter)));
            }
        });

        columns.add(new PropertyColumn<ReleaseFlagHistory, String>(new Model<>("Release Name"),
                "releaseId", "releaseId") {
            @Override
            public void populateItem(final Item<ICellPopulator<ReleaseFlagHistory>> item,
                                     final String componentId,
                                     final IModel<ReleaseFlagHistory> rowModel) {
                item.add(new Label(componentId, Model.of(releaseFlagNotificationService.getReleaseTitle(
                        rowModel.getObject().getReleaseId()))));
            }
        });

        columns.add(new PropertyColumn<>(new Model<>("Release ID"), "releaseId", "releaseId"));
        columns.add(new PropertyColumn<>(new Model<>("Flags Set"), "flagged", "flagged"));

        super.onInitialize();

        //columns.remove(6);
    }
}
