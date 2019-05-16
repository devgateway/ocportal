package org.devgateway.toolkit.forms.wicket.page.overview.department;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapBookmarkablePageLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.FormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.wicket.components.util.SessionUtil;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditCabinetPaperPage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditProcurementPlanPage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditProjectPage;
import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.dao.form.Project;
import org.devgateway.toolkit.persistence.service.category.FiscalYearService;
import org.devgateway.toolkit.persistence.service.form.ProcurementPlanService;
import org.devgateway.toolkit.persistence.service.form.ProjectService;

import java.util.List;
import java.util.stream.Collectors;

public class DepartmentOverviewMainPanel extends Panel {
    private FiscalYear fiscalYear;

    private Department department;

    private ProcurementPlan procurementPlan;

    private List<FiscalYear> fiscalYears;

    private TextField<String> searchBox;

    private List<Project> projects;

    private ListView<Project> projectList;

    @SpringBean
    private ProjectService projectService;

    @SpringBean
    private FiscalYearService fiscalYearService;

    @SpringBean
    private ProcurementPlanService procurementPlanService;

    public DepartmentOverviewMainPanel(final String id) {
        super(id);

        // TODO (params) - extract fiscalYear and department from Session - if not present redirect the user to
        // another page like StatusOverview.
        this.department = SessionUtil.getSessionDepartment();
        this.fiscalYear = SessionUtil.getSessionFiscalYear();

        // years with data for department
        fiscalYears = fiscalYearService.getYearsWithData(department);
        // TODO - add checks when no PP is found for (department, fiscalYear)
        procurementPlan = procurementPlanService.findByDepartmentAndFiscalYear(department, fiscalYear);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        addProcurementPlanButton("newProcurementPlan", null);
        addProcurementPlanButton("editProcurementPlan", procurementPlan.getId());
        addProcurementPlanButton("viewProcurementPlan", procurementPlan.getId());
        addCabinetPaperButton();
        addProjectButton();
        addYearDropdown();
        addSearchBox();
        add(new Label("departmentLabel", procurementPlan.getDepartment().getLabel()));
        add(new Label("procurementPlanLabel", procurementPlan.toString()));
        addProjectList();
    }

    private void addProcurementPlanButton(final String buttonId, final Long procurementPlanId) {
        final PageParameters pageParameters = new PageParameters();
        pageParameters.set(WebConstants.PARAM_ID, procurementPlanId);

        final BootstrapBookmarkablePageLink<Void> newProcurementPlanButton = new BootstrapBookmarkablePageLink<>(
                buttonId, EditProcurementPlanPage.class, pageParameters, Buttons.Type.Success);
        add(newProcurementPlanButton);
    }

    private void addCabinetPaperButton() {
        // TODO (params) - check that here the Cabinet Paper page has access to Procurement Plan
        final BootstrapBookmarkablePageLink<Void> editCabinetPaper = new BootstrapBookmarkablePageLink<>(
                "editCabinetPaper", EditCabinetPaperPage.class, Buttons.Type.Success);
        add(editCabinetPaper);
    }

    private void addProjectButton() {
        final BootstrapBookmarkablePageLink<Void> addNewProject = new BootstrapBookmarkablePageLink<>(
                "addNewProject", EditProjectPage.class, Buttons.Type.Success);
        addNewProject.setLabel(new StringResourceModel("addNewProject", DepartmentOverviewMainPanel.this, null));
        add(addNewProject);
    }

    private void addYearDropdown() {
        final ChoiceRenderer<FiscalYear> choiceRenderer = new ChoiceRenderer<>("label", "id");
        final DropDownChoice<FiscalYear> yearsDropdown = new DropDownChoice("years",
                new PropertyModel<FiscalYear>(this, "fiscalYear"), fiscalYears, choiceRenderer);
        yearsDropdown.add(new FormComponentUpdatingBehavior() {
            @Override
            protected void onUpdate() {
                SessionUtil.setSessionFiscalYear(yearsDropdown.getModelObject());
                setResponsePage(DepartmentOverviewPage.class);

            }
        });
        add(yearsDropdown);
    }

    private void addSearchBox() {
        searchBox = new TextField<>("searchBox", Model.of(""));
        searchBox.add(new FormComponentUpdatingBehavior() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate() {
                if (projects != null) {
                    final List<Project> filteredProjects = projects.stream().filter(p -> {
                        if (p.getProjectTitle() != null && searchBox.getModelObject() != null) {
                            return p.getProjectTitle().toLowerCase().contains(searchBox.getModelObject().toLowerCase());
                        }

                        return true;
                    }).collect(Collectors.toList());

                    projectList.setModelObject(filteredProjects);
                }
            }
        });
        add(searchBox);
    }

    private void addProjectList() {
        projects = projectService.findByProcurementPlan(procurementPlan);
        projectList = new ListView<Project>("projectList", projects) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(final ListItem<Project> item) {
                item.add(new DepartmentOverviewItem("project", item.getModelObject()));
            }
        };
        projectList.setOutputMarkupId(true);
        add(projectList);
    }

    public FiscalYear getFiscalYear() {
        return fiscalYear;
    }

    public void setFiscalYear(final FiscalYear fiscalYear) {
        this.fiscalYear = fiscalYear;
    }
}
