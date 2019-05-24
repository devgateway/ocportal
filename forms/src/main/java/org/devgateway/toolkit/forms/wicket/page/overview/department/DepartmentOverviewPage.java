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
package org.devgateway.toolkit.forms.wicket.page.overview.department;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapBookmarkablePageLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.list.BootstrapListView;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.wicket.components.util.SessionUtil;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditCabinetPaperPage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditProcurementPlanPage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditProjectPage;
import org.devgateway.toolkit.forms.wicket.page.overview.DataEntryBasePage;
import org.devgateway.toolkit.forms.wicket.page.overview.status.StatusOverviewPage;
import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.dao.form.CabinetPaper;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.dao.form.Project;
import org.devgateway.toolkit.persistence.service.category.FiscalYearService;
import org.devgateway.toolkit.persistence.service.filterstate.form.ProjectFilterState;
import org.devgateway.toolkit.persistence.service.form.CabinetPaperService;
import org.devgateway.toolkit.persistence.service.form.ProcurementPlanService;
import org.devgateway.toolkit.persistence.service.form.ProjectService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gmutuhu
 *
 */
@MountPath("/departmentOverview")
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
public class DepartmentOverviewPage extends DataEntryBasePage {
    private FiscalYear fiscalYear;

    private final Department department;

    private final ProcurementPlan procurementPlan;

    private final List<FiscalYear> fiscalYears;

    private String searchBox = "";

    private ListViewProjectsOverview listViewProjectsOverview;

    @SpringBean
    private ProjectService projectService;

    @SpringBean
    private FiscalYearService fiscalYearService;

    @SpringBean
    private ProcurementPlanService procurementPlanService;

    @SpringBean
    private CabinetPaperService cabinetPaperService;

    // TODO all list view should have LoadableDetachableModel models
    public DepartmentOverviewPage(final PageParameters parameters) {
        super(parameters);

        this.department = SessionUtil.getSessionDepartment();
        this.fiscalYear = SessionUtil.getSessionFiscalYear();

        // redirect user to status dashboard page if we don't have all the needed info
        if (this.department == null) {
            logger.warn("User landed on DepartmentOverviewPage page without having any department in Session");
            setResponsePage(StatusOverviewPage.class);
        }

        // get years with data for current department
        fiscalYears = fiscalYearService.getYearsWithData(department);
        procurementPlan = procurementPlanService.findByDepartmentAndFiscalYear(department, fiscalYear);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        add(new Label("departmentLabel", department == null ? "" : department.getLabel()));
        add(new Label("fiscalYear", fiscalYear == null ? "" : fiscalYear.getLabel()));

        addNewProcurementPlanButton();
        addEditProcurementPlanButton();

        addLabelOrInvisibleContainer("procurementPlanLabel", procurementPlan);

        addCabinetPaperButton();
        addProjectButton();
        addYearDropdown();
        addSearchBox();

        addProjectList();
    }

    private void addLabelOrInvisibleContainer(final String id, final Object o) {
        if (o != null) {
            add(new Label(id, o.toString()));
        } else {
            add(new WebMarkupContainer(id).setVisibilityAllowed(false));
        }
    }

    private void addNewProcurementPlanButton() {
        final BootstrapBookmarkablePageLink<Void> newProcurementPlanButton = new BootstrapBookmarkablePageLink<>(
                "newProcurementPlan", EditProcurementPlanPage.class, Buttons.Type.Success);
        add(newProcurementPlanButton);
    }

    private void addEditProcurementPlanButton() {
        final PageParameters pp = new PageParameters();
        if (procurementPlan != null) {
            pp.set(WebConstants.PARAM_ID, procurementPlan.getId());
        }
        final BootstrapBookmarkablePageLink<Void> button = new BootstrapBookmarkablePageLink<>(
                "editProcurementPlan", EditProcurementPlanPage.class, pp, Buttons.Type.Info);
        if (procurementPlan == null) {
            button.setEnabled(false);
        }

        add(button);

        DeptOverviewStatusLabel procurementPlanStatus = new DeptOverviewStatusLabel(
                "procurementPlanStatus", procurementPlan);
        add(procurementPlanStatus);
    }

    private void addCabinetPaperButton() {
        final BootstrapAjaxLink<Void> editCabinetPaper = new BootstrapAjaxLink<Void>("editCabinetPaper",
                Buttons.Type.Success) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                SessionUtil.setSessionPP(procurementPlan);
                setResponsePage(EditCabinetPaperPage.class);
            }
        };
        editCabinetPaper.setEnabled(procurementPlan != null);
        add(editCabinetPaper);

        final LoadableDetachableModel<List<CabinetPaper>> cabinetPapersModel =
                new LoadableDetachableModel<List<CabinetPaper>>() {
                    @Override
                    protected List<CabinetPaper> load() {
                        return procurementPlan == null
                                ? new ArrayList<>()
                                : cabinetPaperService.findByProcurementPlan(procurementPlan);
                    }
                };

        final ListView<CabinetPaper> cabinetPapers =
                new BootstrapListView<CabinetPaper>("cabinetPapers", cabinetPapersModel) {
                    @Override
                    protected void populateItem(ListItem<CabinetPaper> item) {
                        item.add(new Label("label", item.getModelObject().getLabel()));

                        BootstrapBookmarkablePageLink<Void> editCabinetPaper = new BootstrapBookmarkablePageLink<>(
                                "edit", EditCabinetPaperPage.class,
                                new PageParameters().set(WebConstants.PARAM_ID, item.getModelObject().getId()),
                                Buttons.Type.Success);
                        item.add(editCabinetPaper);
                    }
                };

        add(cabinetPapers);
    }

    private void addProjectButton() {
        SessionUtil.setSessionPP(procurementPlan);
        final BootstrapBookmarkablePageLink<Void> addNewProject = new BootstrapBookmarkablePageLink<>(
                "addNewProject", EditProjectPage.class, Buttons.Type.Success);
        addNewProject.setLabel(new StringResourceModel("addNewProject", DepartmentOverviewPage.this, null));
        addNewProject.setEnabled(procurementPlan != null);
        add(addNewProject);
    }

    private void addYearDropdown() {
        final ChoiceRenderer<FiscalYear> choiceRenderer = new ChoiceRenderer<>("label", "id");
        final DropDownChoice<FiscalYear> yearsDropdown = new DropDownChoice("years",
                new PropertyModel<FiscalYear>(this, "fiscalYear"), fiscalYears, choiceRenderer);

        yearsDropdown.add(new AjaxFormComponentUpdatingBehavior("change") {
            @Override
            protected void onUpdate(final AjaxRequestTarget target) {
                SessionUtil.setSessionFiscalYear(fiscalYear);
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

    private void addProjectList() {
        listViewProjectsOverview = new ListViewProjectsOverview("projectsOverview", new ListModel<>(fetchData()));
        add(listViewProjectsOverview);
    }

    private void updateDashboard(final AjaxRequestTarget target) {
        listViewProjectsOverview.setModelObject(fetchData());

        target.add(listViewProjectsOverview);
    }

    private List<Project> fetchData() {
        return procurementPlan == null
                ? new ArrayList<>()
                : projectService.findAll(new ProjectFilterState(procurementPlan, searchBox).getSpecification());
    }
}
