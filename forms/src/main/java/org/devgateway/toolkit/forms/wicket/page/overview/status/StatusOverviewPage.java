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

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapBookmarkablePageLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.components.util.SessionUtil;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditProcurementPlanPage;
import org.devgateway.toolkit.forms.wicket.page.overview.DataEntryBasePage;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.service.category.FiscalYearService;
import org.devgateway.toolkit.persistence.service.overview.StatusOverviewService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.List;

/**
 * @author gmutuhu
 *
 */
@MountPath("/statusOverview")
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
public class StatusOverviewPage extends DataEntryBasePage {
    private final List<FiscalYear> fiscalYears;

    private FiscalYear fiscalYear;

    private TextField<String> searchBox;

    private ListViewStatusOverview listViewStatusOverview;

    @SpringBean
    private FiscalYearService fiscalYearService;

    @SpringBean
    private StatusOverviewService statusOverviewService;

    public StatusOverviewPage(final PageParameters parameters) {
        super(parameters);

        this.fiscalYears = fiscalYearService.getYearsWithData();

        // check if we already have a FY in the session and use that one, otherwise get the last one from DB
        this.fiscalYear = SessionUtil.getSessionFiscalYear();
        if (this.fiscalYear == null) {
            fiscalYear = fiscalYearService.getLastFiscalYear();
            SessionUtil.setSessionFiscalYear(fiscalYear);
        }
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        final BootstrapBookmarkablePageLink<Void> newProcurementPlanButton = new BootstrapBookmarkablePageLink<>(
                "newProcurementPlan", EditProcurementPlanPage.class, Buttons.Type.Success);
        newProcurementPlanButton.setLabel(new StringResourceModel("newProcurementPlan", StatusOverviewPage.this, null));
        add(newProcurementPlanButton);


        addSearchBox();
        addYearDropdown();

        listViewStatusOverview = new ListViewStatusOverview("statusPanel", new ListModel<>(
                statusOverviewService.getAllProjectsByFiscalYear(fiscalYear)
        ));
        add(listViewStatusOverview);
    }

    private void addYearDropdown() {
        final ChoiceRenderer<FiscalYear> choiceRenderer = new ChoiceRenderer<>("label", "id");
        final DropDownChoice<FiscalYear> yearsDropdown = new DropDownChoice("yearsDropdown",
                new PropertyModel<FiscalYear>(this, "fiscalYear"), fiscalYears, choiceRenderer);
        yearsDropdown.add(new AjaxFormComponentUpdatingBehavior("change") {
            @Override
            protected void onUpdate(final AjaxRequestTarget target) {
                SessionUtil.setSessionFiscalYear(yearsDropdown.getModelObject());

                listViewStatusOverview.setModelObject(statusOverviewService
                        .getAllProjectsByFiscalYear(yearsDropdown.getModelObject()));
                target.add(listViewStatusOverview);
            }
        });
        add(yearsDropdown);
    }

    private void addSearchBox() {
        searchBox = new TextField<>("searchBox", Model.of(""));
        searchBox.add(new AjaxFormComponentUpdatingBehavior("change") {
            @Override
            protected void onUpdate(final AjaxRequestTarget target) {
                logger.error(">>>>>>> " + searchBox.getModelObject());
                // departmentsList.setModelObject(statusOverviewData);
            }
        });
        add(searchBox);
    }

    public FiscalYear getFiscalYear() {
        return fiscalYear;
    }

    public void setFiscalYear(final FiscalYear fiscalYear) {
        this.fiscalYear = fiscalYear;
    }
}
