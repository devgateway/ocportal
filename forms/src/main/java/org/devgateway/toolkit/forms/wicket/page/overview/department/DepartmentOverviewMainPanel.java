package org.devgateway.toolkit.forms.wicket.page.overview.department;

import java.util.List;

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
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditProcurementPlanPage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditProjectPage;
import org.devgateway.toolkit.forms.wicket.page.lists.form.ListCabinetPaperPage;
import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.dao.form.Project;
import org.devgateway.toolkit.persistence.service.category.DepartmentService;
import org.devgateway.toolkit.persistence.service.category.FiscalYearService;
import org.devgateway.toolkit.persistence.service.form.ProcurementPlanService;
import org.devgateway.toolkit.persistence.service.form.ProjectService;
import org.devgateway.toolkit.persistence.service.overview.StatusOverviewService;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapBookmarkablePageLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;

public class DepartmentOverviewMainPanel extends Panel {
    private static final long serialVersionUID = 1L;
    @SpringBean
    private StatusOverviewService statusOverviewService;
    private FiscalYear selectedFiscalYear;  
    private List<FiscalYear> fiscalYears;
    private TextField<String> searchBox;
    private ProcurementPlan procurementPlan;
    @SpringBean
    private DepartmentService departmentService;
    
    @SpringBean 
    private ProjectService projectService;
    
    @SpringBean
    private FiscalYearService fiscalYearService;
    
    @SpringBean
    private ProcurementPlanService procurementPlanService;
    
    private ListView<Project> projectList;
    private Department department;
    
    public DepartmentOverviewMainPanel(final String id, final Long departmentId, final Long fiscalYearId) {
        super(id);
        if (fiscalYearId == null) {
            selectedFiscalYear = fiscalYearService.getLastFiscalYear();  
        } else {
            selectedFiscalYear = fiscalYearService.findById(fiscalYearId).orElse(null);
        }
        
        department = departmentService.findById(departmentId).get();
        //years with data for department
        fiscalYears = fiscalYearService.getYearsWithData(departmentId);
        procurementPlan = procurementPlanService.findByDepartmentAndFiscalYear(department, selectedFiscalYear);
        
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
        add(new Label("departmentLabel", Model.of(procurementPlan.getDepartment().getLabel())));
        add(new Label("procurementPlanLabel", Model.of(procurementPlan.toString())));   
        addProjectList();
    }   
    
   private void addProcurementPlanButton(final String buttonId, final Long procurementPlanId) {
        final PageParameters pageParameters = new PageParameters();
        pageParameters.set(WebConstants.PARAM_ID, procurementPlanId);
        final BootstrapBookmarkablePageLink<Void> newProcurementPlanButton = new BootstrapBookmarkablePageLink<Void>(
                buttonId, EditProcurementPlanPage.class, pageParameters, Buttons.Type.Success);        
        add(newProcurementPlanButton);
    }
   
   private void addCabinetPaperButton() {
       final PageParameters pageParameters = new PageParameters();
       pageParameters.set(WebConstants.PARAM_ID, null);
       final BootstrapBookmarkablePageLink<Void> editCabinetPaper = new BootstrapBookmarkablePageLink<Void>(
               "editCabinetPaper", ListCabinetPaperPage.class, pageParameters, Buttons.Type.Success);      
       add(editCabinetPaper);
   }
   
   private void addProjectButton() {
       final PageParameters pageParameters = new PageParameters();
       pageParameters.set(WebConstants.PARAM_ID, null);
       pageParameters.set(WebConstants.PARAM_PROCUREMENT_PLAN_ID, procurementPlan.getId());       
       final BootstrapBookmarkablePageLink<Void> addNewProject = new BootstrapBookmarkablePageLink<Void>(
               "addNewProject", EditProjectPage.class, pageParameters, Buttons.Type.Success);
       addNewProject.setLabel(new StringResourceModel("addNewProject", DepartmentOverviewMainPanel.this, null));       
       add(addNewProject);
   }
   
   private void addYearDropdown() {
       final ChoiceRenderer<FiscalYear> choiceRenderer = new ChoiceRenderer<FiscalYear>("label", "id");
       final DropDownChoice<FiscalYear> yearsDropdown = new DropDownChoice("years",
               new PropertyModel<FiscalYear>(this, "selectedFiscalYear"), fiscalYears, choiceRenderer);
       yearsDropdown.add(new FormComponentUpdatingBehavior() {
           private static final long serialVersionUID = 1L;

           @Override
           protected void onUpdate() {
               selectedFiscalYear = yearsDropdown.getModelObject();                   
               PageParameters pageParameters = this.getFormComponent().getPage().getPageParameters();
               pageParameters.set(WebConstants.PARAM_FISCAL_YEAR_ID, selectedFiscalYear.getId());
               setResponsePage(DepartmentOverviewPage.class, pageParameters);
               
           }
       });
       add(yearsDropdown);
   }

   private void addSearchBox() {
       searchBox = new TextField<String>("searchBox", Model.of(""));
       searchBox.add(new FormComponentUpdatingBehavior() {
                   private static final long serialVersionUID = 1L;
                   @Override
                   protected void onUpdate() {
                      // departmentsList.setModelObject(departmentOverviewData);                        
                   }
               });
       add(searchBox);
   }

   private void addProjectList() {
       List<Project> projects = projectService.findByProcurementPlan(procurementPlan);
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
   public FiscalYear getDefaultYearfilter() {
       return selectedFiscalYear;
   }

   public void setDefaultYearfilter(final FiscalYear selectedFiscalYear) {
       this.selectedFiscalYear = selectedFiscalYear;
   }
}
