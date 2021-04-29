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
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.fm.DgFmBehavior;
import org.devgateway.toolkit.forms.wicket.page.DataExportPage;
import org.devgateway.toolkit.forms.wicket.page.overview.AbstractListViewStatus;
import org.devgateway.toolkit.forms.wicket.page.overview.DataEntryBasePage;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.dto.StatusOverviewRowGroup;
import org.devgateway.toolkit.persistence.service.category.FiscalYearService;
import org.devgateway.toolkit.persistence.service.form.TenderProcessService;
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

    @SpringBean
    private FiscalYearService fiscalYearService;

    @SpringBean
    private TenderProcessService tenderProcessService;

    @SpringBean
    private StatusOverviewService statusOverviewService;


    public abstract class StatusOverviewSearchView extends GenericPanel<Void> {

        protected ListViewStatusOverview listViewStatusOverview;
        protected WebMarkupContainer noData;
        protected String searchBox = "";

        public StatusOverviewSearchView(String id) {
            super(id);
        }

        public WebMarkupContainer createNoData(AbstractListViewStatus<?> lvs) {
            noData = new WebMarkupContainer("noData") {
                @Override
                public boolean isVisible() {
                   return lvs.getModelObject().isEmpty();
                }
            };
            noData.setOutputMarkupId(true);
            noData.setOutputMarkupPlaceholderTag(true);
            return noData;
        }

        protected DropDownChoice<FiscalYear> createYearDropdown() {
            final ChoiceRenderer<FiscalYear> choiceRenderer = new ChoiceRenderer<>("label", "id");
            final DropDownChoice<FiscalYear> yearsDropdown = new DropDownChoice<>("yearsDropdown",
                    fiscalYearModel, fiscalYearsModel, choiceRenderer);
            yearsDropdown.add(new AjaxFormComponentUpdatingBehavior("change") {
                @Override
                protected void onUpdate(final AjaxRequestTarget target) {
                    sessionMetadataService.setSessionFiscalYear(yearsDropdown.getModelObject());
                    fiscalYearModel.setObject(yearsDropdown.getModelObject());
                    updateDashboard(target);
                }
            });
            return yearsDropdown;
        }

        protected TextField<String> createSearchBox() {
            final TextField<String> searchBoxField = new TextField<>("searchBox",
                    new PropertyModel<>(this, "searchBox"));
            searchBoxField.add(new AjaxFormComponentUpdatingBehavior("change") {
                @Override
                protected void onUpdate(final AjaxRequestTarget target) {
                    updateDashboard(target);
                }
            });
            searchBoxField.add(AttributeAppender.append("placeholder",
                    new StringResourceModel(getSearchPlaceholderTrnKey(), StatusOverviewPage.this)
                            .getString()));
            return searchBoxField;
        }

        public abstract String getSearchPlaceholderTrnKey();
        public void updateDashboard(AjaxRequestTarget target) {
            listViewStatusOverview.setModelObject(fetchData());
            listViewStatusOverview.removeListItems();

            sideBar.refreshCounts(target);
            target.add(listViewStatusOverview);
            target.add(noData);
        }

        public abstract List<StatusOverviewRowGroup> fetchData();
        public abstract Boolean tenderProcessView();

        @Override
        protected void onInitialize() {
            super.onInitialize();

            add(createSearchBox());
            add(createYearDropdown());
            add(createDataExport());

            IModel<List<StatusOverviewRowGroup>> dataListModel = new LoadableDetachableModel<List
                    <StatusOverviewRowGroup>>() {
                @Override
                protected List<StatusOverviewRowGroup> load() {
                    return fetchData();
                }
            };

            listViewStatusOverview = new ListViewStatusOverview("statusPanel", dataListModel, tenderProcessView());
            add(listViewStatusOverview);

            add(createNoData(listViewStatusOverview));
        }
    }
    public class StatusOverviewTenderProcessView extends StatusOverviewSearchView {

        @Override
        public void updateDashboard(final AjaxRequestTarget target) {
            sideBar.getTenderProcessCount()
                    .setDefaultModelObject(tenderProcessService.count(statusOverviewService
                            .getTenderProcessViewSpecification(null, getFiscalYear(), searchBox)));

            super.updateDashboard(target);
        }

        @Override
        public List<StatusOverviewRowGroup> fetchData() {
            return statusOverviewService.getDisplayableTenderProcesses(getFiscalYear(), searchBox);
        }

        @Override
        public Boolean tenderProcessView() {
            return true;
        }


        public StatusOverviewTenderProcessView(String id) {
            super(id);
            add(new DgFmBehavior("statusOverview.tenderProcess"));
        }

        @Override
        public String getSearchPlaceholderTrnKey() {
            return "tenderProcessSearchPlaceholder";
        }
    }


    public class StatusOverviewProjectView extends StatusOverviewSearchView {

        public StatusOverviewProjectView(String id) {
            super(id);
            add(new DgFmBehavior("statusOverview.project"));
        }

        @Override
        public void updateDashboard(final AjaxRequestTarget target) {
            sideBar.getProjectCount()
                    .setDefaultModelObject(statusOverviewService.countProjects(getFiscalYear(), searchBox));
            super.updateDashboard(target);
        }

        @Override
        public List<StatusOverviewRowGroup> fetchData() {
            return statusOverviewService.getAllProjects(getFiscalYear(), searchBox);

        }

        @Override
        public Boolean tenderProcessView() {
            return false;
        }

        @Override
        public String getSearchPlaceholderTrnKey() {
            return "projectSearchPlaceholder";
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

        add(new StatusOverviewProjectView("statusOverviewProjectView"));
        add(new StatusOverviewTenderProcessView("statusOverviewTenderProcessView"));

    }
}
