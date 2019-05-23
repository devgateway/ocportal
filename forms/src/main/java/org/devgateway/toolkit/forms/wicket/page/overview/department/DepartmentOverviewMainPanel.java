package org.devgateway.toolkit.forms.wicket.page.overview.department;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapBookmarkablePageLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.list.BootstrapListView;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.TransparentWebMarkupContainer;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.wicket.components.util.SessionUtil;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditCabinetPaperPage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditProcurementPlanPage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditProjectPage;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapAjaxLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapBookmarkablePageLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.list.BootstrapListView;

import java.util.List;

public class DepartmentOverviewMainPanel extends Panel {
    private static final Logger logger = LoggerFactory.getLogger(DepartmentOverviewMainPanel.class);

    private FiscalYear fiscalYear;

    private Department department;

    private ProcurementPlan procurementPlan;

    private List<FiscalYear> fiscalYears;
    private ListView<CabinetPaper> cabinetPapers;

    private ListView<Project> projectsListView;

    @SpringBean
    private ProjectService projectService;

    @SpringBean
    private FiscalYearService fiscalYearService;

    @SpringBean
    private ProcurementPlanService procurementPlanService;

    private String searchText = "";

    private WebMarkupContainer listWrapper;

    @SpringBean
    private CabinetPaperService cabinetPaperService;

    public DepartmentOverviewMainPanel(final String id) {
        super(id);

        this.department = SessionUtil.getSessionDepartment();
        this.fiscalYear = SessionUtil.getSessionFiscalYear();
        if (this.department == null || this.fiscalYear == null) {
           logger.warn("Can not open the department overview page because the department or the fiscal year is null.");
           setResponsePage(StatusOverviewPage.class);
        }

        // redirect user to status dashboard page if we don't have all the needed info
        if (this.department == null) {
            logger.warn("User landed on DepartmentOverviewPage page without having any department in Session");
            setResponsePage(StatusOverviewPage.class);
        }

        // get years with data for current department
        fiscalYears = fiscalYearService.getYearsWithData(department);
        procurementPlan = procurementPlanService.findByDepartmentAndFiscalYear(department, fiscalYear);
    }


    public void addLabelOrInvisibleContainer(String id, Object o) {
        if (o != null) {
            add(new Label(id, o.toString()));
        } else {
            add(new WebMarkupContainer(id).setVisibilityAllowed(false));
        }
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        addNewProcurementPlanButton();
        addEditProcurementPlanButton();
        addViewProcurementPlanButton();
        addCabinetPaperButton();
        addProjectButton();
        addYearDropdown();
        addSearchBox();
        add(new Label("departmentLabel", department != null ? department.getLabel() : ""));
        add(new Label("fiscalYear", fiscalYear != null ? fiscalYear.getLabel() : ""));

        addLabelOrInvisibleContainer("procurementPlanLabel", procurementPlan);
        addProjectList();
    }

    private void addNewProcurementPlanButton() {
        final BootstrapBookmarkablePageLink<Void> newProcurementPlanButton = new BootstrapBookmarkablePageLink<>(
                "newProcurementPlan", EditProcurementPlanPage.class, Buttons.Type.Success);
        add(newProcurementPlanButton);
    }

    private void addEditProcurementPlanButton() {
        PageParameters pp = new PageParameters();
        if (procurementPlan != null) {
            pp.set(WebConstants.PARAM_ID, procurementPlan.getId());
        }
        final BootstrapBookmarkablePageLink<Void> button = new BootstrapBookmarkablePageLink<>(
                "editProcurementPlan", EditProcurementPlanPage.class, pp, Buttons.Type.Info);
        if (procurementPlan == null) {
            button.setEnabled(false);
        }
        add(button);
    }

    private void addViewProcurementPlanButton() {
        PageParameters pp = new PageParameters();
        if (procurementPlan != null) {
            pp.set(WebConstants.PARAM_ID, procurementPlan.getId());
        }

        DeptOverviewStatusLabel procurementPlanStatus = new DeptOverviewStatusLabel(
                "procurementPlanStatus",
                procurementPlan
        );
        add(procurementPlanStatus);

        final BootstrapBookmarkablePageLink<Void> button = new BootstrapBookmarkablePageLink<>(
                "viewProcurementPlan", EditProcurementPlanPage.class, pp, Buttons.Type.Success);
        if (procurementPlan == null) {
            button.setEnabled(false);
        }
        add(button);
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

        LoadableDetachableModel<List<CabinetPaper>> cabinetPapersModel =
                new LoadableDetachableModel<List<CabinetPaper>>() {
                    @Override
                    protected List<CabinetPaper> load() {
                        return cabinetPaperService.findByProcurementPlan(procurementPlan);
                    }
                };

        cabinetPapers = new BootstrapListView<CabinetPaper>("cabinetPapers", cabinetPapersModel) {
            @Override
            protected void populateItem(ListItem<CabinetPaper> item) {
                item.add(new Label("label", item.getModelObject().getLabel()));

                BootstrapBookmarkablePageLink<Void> editCabinetPaper = new BootstrapBookmarkablePageLink<>(
                        "edit", EditCabinetPaperPage.class,
                        new PageParameters().set(WebConstants.PARAM_ID, item.getModelObject().getId()),
                        Buttons.Type.Success
                );
                item.add(editCabinetPaper);
            }
        };

        add(cabinetPapers);

    }

    private void addProjectButton() {
        SessionUtil.setSessionPP(procurementPlan);
        final BootstrapBookmarkablePageLink<Void> addNewProject = new BootstrapBookmarkablePageLink<>(
                "addNewProject", EditProjectPage.class, Buttons.Type.Success);
        addNewProject.setLabel(new StringResourceModel("addNewProject", DepartmentOverviewMainPanel.this, null));
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
        final TextField<String> searchBoxField = new TextField<>("searchBox", new PropertyModel<>(this, "searchText"));
        searchBoxField.add(new AjaxFormComponentUpdatingBehavior("change") {
            @Override
            protected void onUpdate(final AjaxRequestTarget target) {
                updateDashboard(target);
            }
        });
        add(searchBoxField);
    }

    private void addProjectList() {
        listWrapper = new TransparentWebMarkupContainer("listWrapper");
        listWrapper.setOutputMarkupId(true);
        add(listWrapper);

        Long procurementPlanId = procurementPlan != null ? procurementPlan.getId() : null;
        List<Project> projects = projectService.findAll(
                new ProjectFilterState(procurementPlanId, searchText)
                        .getSpecification());

        projectsListView = new ListView<Project>("projectList", projects) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(final ListItem<Project> item) {
                item.add(new DepartmentOverviewItem("project", item.getModelObject()));
            }
        };
        projectsListView.setOutputMarkupId(true);
        listWrapper.add(projectsListView);
    }

    public FiscalYear getFiscalYear() {
        return fiscalYear;
    }

    public void setFiscalYear(final FiscalYear fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    private void updateDashboard(final AjaxRequestTarget target) {
        Long procurementPlanId = procurementPlan != null ? procurementPlan.getId() : null;
        projectsListView.setModelObject(projectService.findAll(
                new ProjectFilterState(procurementPlanId, searchText)
                        .getSpecification()));
        target.add(listWrapper);
    }
}
