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
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.page.overview.DataEntryBasePage;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.dto.StatusOverviewData;
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

    private ListViewStatusOverview listViewStatusOverview;

    @SpringBean
    private FiscalYearService fiscalYearService;

    @SpringBean
    private StatusOverviewService statusOverviewService;

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

    @Override
    protected void onInitialize() {
        super.onInitialize();

        addSearchBox();
        addYearDropdown();

        listViewStatusOverview = new ListViewStatusOverview("statusPanel", new ListModel<>(fetchData()));
        add(listViewStatusOverview);
    }

    private void addYearDropdown() {
        final ChoiceRenderer<FiscalYear> choiceRenderer = new ChoiceRenderer<>("label", "id");
        final DropDownChoice<FiscalYear> yearsDropdown = new DropDownChoice("yearsDropdown",
                fiscalYearModel, fiscalYearsModel, choiceRenderer);
        yearsDropdown.add(new AjaxFormComponentUpdatingBehavior("change") {
            @Override
            protected void onUpdate(final AjaxRequestTarget target) {
                sessionMetadataService.setSessionFiscalYear(yearsDropdown.getModelObject());
                fiscalYearModel.setObject(yearsDropdown.getModelObject());
                updateDashboard(target);
            }
        });
        add(yearsDropdown);
    }

    private void addSearchBox() {
        final TextField<String> searchBoxField = new TextField<>("searchBox", new PropertyModel<>(this, "searchBox"));
        searchBoxField.add(new AjaxFormComponentUpdatingBehavior("change") {
            @Override
            protected void onUpdate(final AjaxRequestTarget target) {
                updateDashboard(target);
            }
        });
        add(searchBoxField);
    }

    private void updateDashboard(final AjaxRequestTarget target) {
        listViewStatusOverview.setModelObject(fetchData());
        listViewStatusOverview.removeListItems();

        // update the project count from sidebar as well
        sideBar.getProjectCount()
                .setDefaultModelObject(statusOverviewService.countProjects(getFiscalYear(), searchBox));

        target.add(listViewStatusOverview);
        target.add(sideBar.getProjectCount());
    }

    private List<StatusOverviewData> fetchData() {
        long startTime = System.nanoTime();

        final List<StatusOverviewData> list = statusOverviewService.getAllProjects(getFiscalYear(), searchBox);

        long endTime = System.nanoTime();
        double duration = (endTime - startTime) / 1000000000.0;
        logger.info("------- [StatusPage] ALL PROCESSING TIME: " + duration);
        logger.info("-------------------------------------------------------------------------------");

        return list;
    }
}
