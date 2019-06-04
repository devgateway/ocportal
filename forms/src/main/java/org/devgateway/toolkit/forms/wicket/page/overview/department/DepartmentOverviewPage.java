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
import de.agilecoders.wicket.core.markup.html.bootstrap.components.TooltipBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.ladda.LaddaAjaxButton;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.IRequestCycle;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.WebConstants;
import org.devgateway.toolkit.forms.wicket.components.form.AJAXDownload;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditCabinetPaperPage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditProcurementPlanPage;
import org.devgateway.toolkit.forms.wicket.page.edit.form.EditProjectPage;
import org.devgateway.toolkit.forms.wicket.page.lists.form.ListCabinetPaperPage;
import org.devgateway.toolkit.forms.wicket.page.overview.DataEntryBasePage;
import org.devgateway.toolkit.forms.wicket.page.overview.status.StatusOverviewPage;
import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.dao.form.Project;
import org.devgateway.toolkit.persistence.excel.service.ExcelGeneratorService;
import org.devgateway.toolkit.persistence.service.filterstate.form.ProjectFilterState;
import org.devgateway.toolkit.persistence.service.form.ProcurementPlanService;
import org.devgateway.toolkit.persistence.service.form.ProjectService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author gmutuhu
 *
 */
@MountPath("/departmentOverview")
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
public class DepartmentOverviewPage extends DataEntryBasePage {

    private final IModel<ProcurementPlan> procurementPlanModel;

    private String searchBox = "";

    private ListViewProjectsOverview listViewProjectsOverview;

    @SpringBean
    private ProjectService projectService;

    @SpringBean
    private ProcurementPlanService procurementPlanService;

    private Label newProcurementPlanLabel;

    protected final IModel<FiscalYear> fiscalYearModel;

    protected IModel<List<FiscalYear>> fiscalYearsModel;

    @SpringBean
    private ExcelGeneratorService excelGeneratorService;

    private Department getDepartment() {
        return sessionMetadataService.getSessionDepartment();
    }

    private FiscalYear getFiscalYear() {
        return sessionMetadataService.getSessionFiscalYear();
    }

    private ProcurementPlan getProcurementPlan() {
        return procurementPlanModel.getObject();
    }

    public DepartmentOverviewPage(final PageParameters parameters) {
        super(parameters);

        fiscalYearsModel = new LoadableDetachableModel<List<FiscalYear>>() {
            @Override
            protected List<FiscalYear> load() {
                return fiscalYearService.findAll();
            }
        };

        fiscalYearModel = new LoadableDetachableModel<FiscalYear>() {
            @Override
            protected FiscalYear load() {
                FiscalYear fiscalYear = sessionMetadataService.getSessionFiscalYear();
                if (fiscalYear != null) {
                    return fiscalYear;
                }
                fiscalYear = fiscalYearsModel.getObject().isEmpty() ? null : fiscalYearsModel.getObject().get(0);
                sessionMetadataService.setSessionFiscalYear(fiscalYear);
                return fiscalYear;
            }
        };

        // redirect user to status dashboard page if we don't have all the needed info
        if (getDepartment() == null) {
            logger.warn("User landed on DepartmentOverviewPage page without having any department in Session");
            setResponsePage(StatusOverviewPage.class);
        }

        procurementPlanModel = new LoadableDetachableModel<ProcurementPlan>() {
            @Override
            protected ProcurementPlan load() {
                return procurementPlanService.findByDepartmentAndFiscalYear(getDepartment(), getFiscalYear());
            }
        };
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        add(new Label("departmentLabel", getDepartment() == null ? "" : getDepartment().getLabel()));
        add(new Label("fiscalYear", new PropertyModel<>(fiscalYearModel, "label")));

        addNewProcurementPlanButton();
        addEditProcurementPlanButton();

        addLabelOrInvisibleContainer("procurementPlanLabel", getProcurementPlan());

        addCabinetPaperButton();
        listCabinetPaperButton();
        addProjectButton();
        addYearDropdown();

        final Form excelForm = new ExcelDownloadForm("excelForm");
        add(excelForm);

        addSearchBox();

        addProjectList();
    }

    /**
     * A wrapper form that is used to fire the excel download action
     */
    public class ExcelDownloadForm extends Form<Void> {
        public ExcelDownloadForm(final String id) {
            super(id);
        }

        @Override
        protected void onInitialize() {
            super.onInitialize();

            final AJAXDownload download = new AJAXDownload() {
                @Override
                protected IRequestHandler getHandler() {
                    return new IRequestHandler() {
                        @Override
                        public void respond(final IRequestCycle requestCycle) {
                            final HttpServletResponse response = (HttpServletResponse) requestCycle
                                    .getResponse().getContainerResponse();

                            try {
                                    final byte[] bytes = excelGeneratorService.getExcelDownload(
                                            new ArrayList<>(Arrays.asList(getProcurementPlan())));

                                    response.setContentType(
                                            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                                    response.setHeader("Content-Disposition", "attachment; filename=excel-export.xlsx");
                                    response.getOutputStream().write(bytes);

                            } catch (IOException e) {
                                logger.error("Download error", e);
                            }

                            RequestCycle.get().scheduleRequestHandlerAfterCurrent(null);
                        }

                        @Override
                        public void detach(final IRequestCycle requestCycle) {
                            // do nothing;
                        }
                    };
                }
            };
            add(download);

            final LaddaAjaxButton excelButton = new LaddaAjaxButton("excelButton", Buttons.Type.Warning) {
                @Override
                protected void onSubmit(final AjaxRequestTarget target) {
                    super.onSubmit(target);

                    // initiate the file download
                    download.initiate(target);
                }
            };
            add(excelButton);
        }
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
        newProcurementPlanButton.setEnabled(getProcurementPlan() == null && getFiscalYear() != null);
        newProcurementPlanButton.setVisibilityAllowed(
                ComponentUtil.canAccessAddNewButtonInDeptOverview(sessionMetadataService));

        newProcurementPlanLabel = new Label("newProcurementPlanLabel", Model.of("Create new procurement plan"));
        newProcurementPlanLabel.setVisibilityAllowed(newProcurementPlanButton.isVisibilityAllowed());
        add(newProcurementPlanLabel);
    }

    private void addEditProcurementPlanButton() {
        final PageParameters pp = new PageParameters();
        if (getProcurementPlan() != null) {
            pp.set(WebConstants.PARAM_ID, getProcurementPlan().getId());
        }
        final BootstrapBookmarkablePageLink<Void> button = new BootstrapBookmarkablePageLink<>(
                "editProcurementPlan", EditProcurementPlanPage.class, pp, Buttons.Type.Info);
        button.setEnabled(getProcurementPlan() != null);
        button.add(new TooltipBehavior(Model.of("Edit/View Procurement Plan")));

        add(button);

        DeptOverviewStatusLabel procurementPlanStatus = new DeptOverviewStatusLabel(
                "procurementPlanStatus", getProcurementPlan());
        add(procurementPlanStatus);
    }

    private void addCabinetPaperButton() {
        final BootstrapAjaxLink<Void> editCabinetPaper = new BootstrapAjaxLink<Void>("editCabinetPaper",
                Buttons.Type.Success) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                sessionMetadataService.setSessionPP(getProcurementPlan());
                setResponsePage(EditCabinetPaperPage.class);
            }
        };
        editCabinetPaper.setEnabled(getProcurementPlan() != null);
        editCabinetPaper.add(new TooltipBehavior(Model.of("Add New Cabinet Paper")));
        add(editCabinetPaper);
        editCabinetPaper.setVisibilityAllowed(
                ComponentUtil.canAccessAddNewButtonInDeptOverview(sessionMetadataService));
    }

    private void listCabinetPaperButton() {
        final BootstrapAjaxLink<Void> editCabinetPaper = new BootstrapAjaxLink<Void>("listCabinetPaper",
                Buttons.Type.Success) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                sessionMetadataService.setSessionPP(getProcurementPlan());
                setResponsePage(ListCabinetPaperPage.class);
            }
        };
        editCabinetPaper.setEnabled(getProcurementPlan() != null);
        editCabinetPaper.add(new TooltipBehavior(Model.of("List Cabinet Papers")));
        add(editCabinetPaper);
    }

    private void addProjectButton() {
        sessionMetadataService.setSessionPP(getProcurementPlan());
        final BootstrapBookmarkablePageLink<Void> addNewProject = new BootstrapBookmarkablePageLink<>(
                "addNewProject", EditProjectPage.class, Buttons.Type.Success);
        addNewProject.setLabel(new StringResourceModel("addNewProject", DepartmentOverviewPage.this, null));
        addNewProject.setEnabled(getProcurementPlan() != null);
        addNewProject.setVisibilityAllowed(ComponentUtil.canAccessAddNewButtonInDeptOverview(sessionMetadataService));
        add(addNewProject);
    }

    private void addYearDropdown() {
        final ChoiceRenderer<FiscalYear> choiceRenderer = new ChoiceRenderer<>("label", "id");
        final DropDownChoice<FiscalYear> yearsDropdown = new DropDownChoice("years",
                fiscalYearModel, fiscalYearsModel, choiceRenderer);

        yearsDropdown.add(new AjaxFormComponentUpdatingBehavior("change") {
            @Override
            protected void onUpdate(final AjaxRequestTarget target) {
                sessionMetadataService.setSessionFiscalYear(yearsDropdown.getModelObject());
                setResponsePage(DepartmentOverviewPage.class);
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
        listViewProjectsOverview = new ListViewProjectsOverview("projectsOverview",
                new ListModel<>(fetchData()), procurementPlanModel);
        add(listViewProjectsOverview);
    }

    private void updateDashboard(final AjaxRequestTarget target) {
        listViewProjectsOverview.setModelObject(fetchData());
        listViewProjectsOverview.removeListItems();

        target.add(listViewProjectsOverview);
    }

    private List<Project> fetchData() {
        return getProcurementPlan() == null
                ? new ArrayList<>()
                : projectService.findAll(new ProjectFilterState(getProcurementPlan(), searchBox).getSpecification());
    }
}
