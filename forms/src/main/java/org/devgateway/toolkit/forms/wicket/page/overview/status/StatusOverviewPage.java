/**
 * Copyright (c) 2015 Development Gateway, Inc and others.
 * <p>
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License (MIT)
 * which accompanies this distribution, and is available at
 * https://opensource.org/licenses/MIT
 * <p>
 * Contributors:
 * Development Gateway - initial API and implementation
 */
/**
 *
 */
package org.devgateway.toolkit.forms.wicket.page.overview.status;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.danekja.java.util.function.serializable.SerializableConsumer;
import org.devgateway.toolkit.forms.wicket.page.DataExportPage;
import org.devgateway.toolkit.forms.wicket.page.overview.AbstractListViewStatus;
import org.devgateway.toolkit.forms.wicket.page.overview.DataEntryBasePage;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.dto.StatusOverviewRowGroup;
import org.devgateway.toolkit.persistence.service.category.FiscalYearService;
import org.devgateway.toolkit.persistence.service.overview.StatusOverviewService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.List;

/**
 * @author gmutuhu
 */
@MountPath("/statusOverview")
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
public class StatusOverviewPage extends DataEntryBasePage {

    private final IModel<List<FiscalYear>> fiscalYearsModel;

    private final IModel<FiscalYear> fiscalYearModel;

    private String searchBox = "";

    private ListViewStatusOverview listViewStatusTenderProcessOverview;
    private ListViewStatusOverview listViewStatusProjectOverview;

    @SpringBean
    private FiscalYearService fiscalYearService;

    @SpringBean
    private StatusOverviewService statusOverviewService;
    private WebMarkupContainer noData;

    public class StatusOverviewSearchView extends GenericPanel<Void> {

        public StatusOverviewSearchView(String id) {
            super(id);
        }
    }
    public class StatusOverviewTenderProcessView extends StatusOverviewSearchView {

        public StatusOverviewTenderProcessView(String id) {
            super(id);
        }

        @Override
        protected void onInitialize() {
            super.onInitialize();
            add(createSearchBox(StatusOverviewPage.this::updateProjectDashboard));
            add(createYearDropdown());
            add(createDataExport());

            IModel<List<StatusOverviewRowGroup>> dataListModel = new LoadableDetachableModel<List<StatusOverviewRowGroup>>() {
                @Override
                protected List<StatusOverviewRowGroup> load() {
                    return fetchTenderProcessData();
                }
            };

            listViewStatusTenderProcessOverview = new ListViewStatusOverview("statusPanel", dataListModel);
            add(listViewStatusTenderProcessOverview);

            add(createNoData(listViewStatusTenderProcessOverview));
        }
    }


    public class StatusOverviewProjectView extends StatusOverviewSearchView {

        public StatusOverviewProjectView(String id) {
            super(id);
        }

        @Override
        protected void onInitialize() {
            super.onInitialize();
            add(createSearchBox());
            add(createYearDropdown());
            add(createDataExport());

            IModel<List<StatusOverviewRowGroup>> dataListModel = new LoadableDetachableModel<List<StatusOverviewRowGroup>>() {
                @Override
                protected List<StatusOverviewRowGroup> load() {
                    return fetchProjectData();
                }
            };

            listViewStatusProjectOverview = new ListViewStatusOverview("statusPanel", dataListModel);
            add(listViewStatusProjectOverview);

            add(createNoData(listViewStatusProjectOverview));
        }
    }

    public StatusOverviewPage(final PageParameters parameters) {
        super(parameters);

        fiscalYearsModel = new LoadableDetachableModel<List<FiscalYear>>() {

            @Override
            protected List<FiscalYear> load() {
                return fiscalYearService.getYearsWithData();
            }
        };

        // we need to se the FY in the session since it's used in other parts like in the SideBar
        if (sessionMetadataService.getSessionFiscalYear() == null) {
            // check if we already have a FY in the session and use that one, otherwise get the last one from DB
            sessionMetadataService.setSessionFiscalYear(fiscalYearService.getLastFiscalYear());
        }
        fiscalYearModel = new LoadableDetachableModel<FiscalYear>() {
            @Override
            protected FiscalYear load() {
                return sessionMetadataService.getSessionFiscalYear();
            }
        };

        // clear department from session
        sessionMetadataService.setSessionDepartment(null);
    }

    private FiscalYear getFiscalYear() {
        return fiscalYearModel.getObject();
    }


    protected Link<Void> createDataExport() {
        final Link<Void> dataExport = new Link<Void>("dataExport") {
            @Override
            public void onClick() {
                setResponsePage(DataExportPage.class);
            }
        };
        return dataExport;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        //add(new StatusOverviewProjectView("statusOverviewProjectView"));
        add(new StatusOverviewTenderProcessView("statusOverviewTenderProcessView"));

    }


    public WebMarkupContainer createNoData(AbstractListViewStatus<?> lvs) {
        noData = new WebMarkupContainer("noData");
        noData.setOutputMarkupId(true);
        noData.setOutputMarkupPlaceholderTag(true);
        noData.setVisibilityAllowed(lvs.getModelObject().isEmpty());
        return noData;
    }

    private DropDownChoice<FiscalYear> createYearDropdown() {
        final ChoiceRenderer<FiscalYear> choiceRenderer = new ChoiceRenderer<>("label", "id");
        final DropDownChoice<FiscalYear> yearsDropdown = new DropDownChoice<>("yearsDropdown",
                fiscalYearModel, fiscalYearsModel, choiceRenderer);
        yearsDropdown.add(new AjaxFormComponentUpdatingBehavior("change") {
            @Override
            protected void onUpdate(final AjaxRequestTarget target) {
                sessionMetadataService.setSessionFiscalYear(yearsDropdown.getModelObject());
                fiscalYearModel.setObject(yearsDropdown.getModelObject());
                updateProjectDashboard(target);
            }
        });
        return yearsDropdown;
    }

    private  TextField<String> createSearchBox(SerializableConsumer<AjaxRequestTarget> consumeUpdate) {
        final TextField<String> searchBoxField = new TextField<>("searchBox", new PropertyModel<>(this, "searchBox"));
        searchBoxField.add(new AjaxFormComponentUpdatingBehavior("change") {
            @Override
            protected void onUpdate(final AjaxRequestTarget target) {
                consumeUpdate.accept(target);
            }
        });
        return searchBoxField;
    }

    private void updateProjectDashboard(final AjaxRequestTarget target) {
        listViewStatusProjectOverview.setModelObject(fetchProjectData());
        listViewStatusProjectOverview.removeListItems();

        // update the project count from sidebar as well
        sideBar.getProjectCount()
                .setDefaultModelObject(statusOverviewService.countProjects(getFiscalYear(), searchBox));

        target.add(listViewStatusProjectOverview);
        target.add(sideBar.getProjectCount());
        target.add(noData);
    }

    private void updateTenderProcessDashboard(final AjaxRequestTarget target) {
        listViewStatusTenderProcessOverview.setModelObject(fetchProjectData());
        listViewStatusTenderProcessOverview.removeListItems();

        sideBar.getTenderProcessCount()
                .setDefaultModelObject(statusOverviewService.countTenderProcesses(getFiscalYear(), searchBox));

        target.add(listViewStatusTenderProcessOverview);
        target.add(sideBar.getTenderProcessCount());
        target.add(noData);
    }

    private List<StatusOverviewRowGroup> fetchProjectData() {
        return statusOverviewService.getAllProjects(getFiscalYear(), searchBox);
    }

    private List<StatusOverviewRowGroup> fetchTenderProcessData() {
        return statusOverviewService.getDisplayableTenderProcesses(getFiscalYear(), searchBox);
    }
}
