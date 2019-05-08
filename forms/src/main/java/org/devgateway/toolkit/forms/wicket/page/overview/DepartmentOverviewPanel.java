package org.devgateway.toolkit.forms.wicket.page.overview;

import java.util.List;

import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.FormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.wicket.components.form.TextFieldBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditProcurementPlanPage;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.dto.DepartmentOverviewData;
import org.devgateway.toolkit.persistence.service.overview.StatusOverviewService;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.BootstrapBookmarkablePageLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;

public class DepartmentOverviewPanel extends Panel {
    private static final long serialVersionUID = 1L;

    @SpringBean
    private StatusOverviewService statusOverviewService;

    private ListView<DepartmentOverviewData> departmentsList;

   
    private FiscalYear defaultYearfilter;

    public DepartmentOverviewPanel(final String id) {
        super(id);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        final List<FiscalYear> fiscalYears = statusOverviewService.getYearsWithData();
        defaultYearfilter = fiscalYears.stream().findFirst().orElse(null);
        addProcurementPlanButton();
        addDepartmentList();
        addYearDropdown(fiscalYears);

        // final TextFieldBootstrapFormComponent<String> searchBox = new
        // TextFieldBootstrapFormComponent<>("searchBox");
        // add(searchBox);
    }

    private void addProcurementPlanButton() {
        final PageParameters pageParameters = new PageParameters();
        pageParameters.set(WebConstants.PARAM_ID, null);
        final BootstrapBookmarkablePageLink<Void> newProcurementPlanButton = new BootstrapBookmarkablePageLink<Void>(
                "newProcurementPlan", EditProcurementPlanPage.class, pageParameters, Buttons.Type.Success);
        newProcurementPlanButton
                .setLabel(new StringResourceModel("newProcurementPlan", DepartmentOverviewPanel.this, null));
        add(newProcurementPlanButton);
    }

    private void addDepartmentList() {
        final Long defaultFiscalYearId = defaultYearfilter != null ? defaultYearfilter.getId() : null;
        List<DepartmentOverviewData> data = statusOverviewService.getProjectsByDepartment(defaultFiscalYearId);
        departmentsList = new ListView<DepartmentOverviewData>("group", data) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(final ListItem<DepartmentOverviewData> item) {
                item.add(new DepartmentGroupItem("groupItem", item.getModelObject()));

            }
        };
        add(departmentsList);
    }

    private void addYearDropdown(final List<FiscalYear> fiscalYears) {

        ChoiceRenderer<FiscalYear> choiceRenderer = new ChoiceRenderer<FiscalYear>("label", "id");
        DropDownChoice<FiscalYear> yearsDropdown = new DropDownChoice("years",
                new PropertyModel<FiscalYear>(this, "defaultYearfilter"), fiscalYears, choiceRenderer);
        yearsDropdown.add(new FormComponentUpdatingBehavior() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate() {
                List<DepartmentOverviewData> data = statusOverviewService
                        .getProjectsByDepartment(yearsDropdown.getModelObject().getId());
                departmentsList.setModelObject(data);
            }
        });
        add(yearsDropdown);
    }


    public FiscalYear getDefaultYearfilter() {
        return defaultYearfilter;
    }

    public void setDefaultYearfilter(final FiscalYear defaultYearfilter) {
        this.defaultYearfilter = defaultYearfilter;
    }

}
