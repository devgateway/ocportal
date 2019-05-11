package org.devgateway.toolkit.forms.wicket.page.overview.status;

import java.util.List;

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
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.dto.DepartmentOverviewData;
import org.devgateway.toolkit.persistence.service.overview.StatusOverviewService;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapBookmarkablePageLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;

public class StatusOverviewMainPanel extends Panel {
    private static final long serialVersionUID = 1L;

    @SpringBean
    private StatusOverviewService statusOverviewService;

    private ListView<DepartmentOverviewData> departmentsList;   
    private FiscalYear defaultYearfilter;    
    private List<DepartmentOverviewData> departmentOverviewData;
    private TextField<String> searchBox;
    
    public StatusOverviewMainPanel(final String id) {
        super(id);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        final List<FiscalYear> fiscalYears = statusOverviewService.getYearsWithData();
        defaultYearfilter = fiscalYears.stream().findFirst().orElse(null);
        addSearchBox();
        addProcurementPlanButton();
        addDepartmentList();
        addYearDropdown(fiscalYears);       
    }

    private void addProcurementPlanButton() {
        final PageParameters pageParameters = new PageParameters();
        pageParameters.set(WebConstants.PARAM_ID, null);
        final BootstrapBookmarkablePageLink<Void> newProcurementPlanButton = new BootstrapBookmarkablePageLink<Void>(
                "newProcurementPlan", EditProcurementPlanPage.class, pageParameters, Buttons.Type.Success);
        newProcurementPlanButton
                .setLabel(new StringResourceModel("newProcurementPlan", StatusOverviewMainPanel.this, null));
        add(newProcurementPlanButton);
    }

    private void addDepartmentList() {
        final Long defaultFiscalYearId = defaultYearfilter != null ? defaultYearfilter.getId() : null;
        departmentOverviewData = statusOverviewService.getProjectsByDepartment(defaultFiscalYearId);
        departmentsList = new ListView<DepartmentOverviewData>("group", departmentOverviewData) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(final ListItem<DepartmentOverviewData> item) {
                item.add(new StatusOverviewItem("groupItem", item.getModelObject(), searchBox.getModelObject()));

            }
        };
        departmentsList.setOutputMarkupId(true);
        add(departmentsList);
    }

    private void addYearDropdown(final List<FiscalYear> fiscalYears) {

        final ChoiceRenderer<FiscalYear> choiceRenderer = new ChoiceRenderer<FiscalYear>("label", "id");
        final DropDownChoice<FiscalYear> yearsDropdown = new DropDownChoice("years",
                new PropertyModel<FiscalYear>(this, "defaultYearfilter"), fiscalYears, choiceRenderer);
        yearsDropdown.add(new FormComponentUpdatingBehavior() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate() {
                departmentOverviewData = statusOverviewService
                        .getProjectsByDepartment(yearsDropdown.getModelObject().getId());
                departmentsList.setModelObject(departmentOverviewData);
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
                        departmentsList.setModelObject(departmentOverviewData);                        
                    }
                });
        add(searchBox);
    }

    public FiscalYear getDefaultYearfilter() {
        return defaultYearfilter;
    }

    public void setDefaultYearfilter(final FiscalYear defaultYearfilter) {
        this.defaultYearfilter = defaultYearfilter;
    }
}
