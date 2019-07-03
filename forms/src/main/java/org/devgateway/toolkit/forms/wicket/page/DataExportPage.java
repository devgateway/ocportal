package org.devgateway.toolkit.forms.wicket.page;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeIconType;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.ladda.LaddaAjaxButton;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.IRequestCycle;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.components.form.AJAXDownload;
import org.devgateway.toolkit.forms.wicket.components.form.Select2ChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.service.category.DepartmentService;
import org.devgateway.toolkit.persistence.service.category.FiscalYearService;
import org.devgateway.toolkit.persistence.service.excel.DataExportService;
import org.devgateway.toolkit.persistence.service.form.ProcurementPlanService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * @author idobre
 * @since 2019-06-07
 */
@MountPath("/dataExport")
@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_USER)
public class DataExportPage extends BasePage {
    private final DataExportBean dataExportBean;

    public DataExportPage(PageParameters parameters) {
        super(parameters);

        dataExportBean = new DataExportBean();
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        final DataExportForm dataExportForm = new DataExportForm("dataExportForm",
                new CompoundPropertyModel<>(dataExportBean));
        add(dataExportForm);
    }

    class DataExportBean implements Serializable {
        private Department department;

        private FiscalYear fiscalYear;

        public Department getDepartment() {
            return department;
        }

        public void setDepartment(final Department department) {
            this.department = department;
        }

        public FiscalYear getFiscalYear() {
            return fiscalYear;
        }

        public void setFiscalYear(final FiscalYear fiscalYear) {
            this.fiscalYear = fiscalYear;
        }
    }

    class DataExportForm extends BootstrapForm<DataExportBean> {
        private Select2ChoiceBootstrapFormComponent<Department> department;

        private Select2ChoiceBootstrapFormComponent<FiscalYear> fiscalYear;

        @SpringBean
        private DataExportService dataExportService;

        @SpringBean
        private ProcurementPlanService procurementPlanService;

        @SpringBean
        private DepartmentService departmentService;

        @SpringBean
        private FiscalYearService fiscalYearService;

        DataExportForm(final String componentId, final IModel<DataExportBean> model) {
            super(componentId, model);
        }

        @Override
        protected void onInitialize() {
            super.onInitialize();

            department = ComponentUtil.addSelect2ChoiceField(this, "department", departmentService);
            department.required();
            add(department);

            fiscalYear = ComponentUtil.addSelect2ChoiceField(this, "fiscalYear", fiscalYearService);
            fiscalYear.required();
            add(fiscalYear);

            final AJAXDownload download = new AJAXDownload() {
                @Override
                protected IRequestHandler getHandler() {
                    return new IRequestHandler() {
                        @Override
                        public void respond(final IRequestCycle requestCycle) {
                            final HttpServletResponse response = (HttpServletResponse) requestCycle
                                    .getResponse().getContainerResponse();

                            try {
                                final Department department = DataExportForm.this.getModelObject().getDepartment();
                                final FiscalYear fiscalYear = DataExportForm.this.getModelObject().getFiscalYear();

                                final byte[] bytes = dataExportService.generateProcurementPlanExcel(
                                        procurementPlanService.findByDepartmentAndFiscalYear(department, fiscalYear)
                                                .getId());

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

            final LaddaAjaxButton excelButton = new LaddaAjaxButton("excelButton",
                    new Model<>("Data Export"),
                    Buttons.Type.Success) {
                @Override
                protected void onConfigure() {
                    super.onConfigure();
                    setDefaultFormProcessing(true);
                }

                @Override
                protected void onSubmit(final AjaxRequestTarget target) {
                    super.onSubmit(target);

                    final Department department = DataExportForm.this.getModelObject().getDepartment();
                    final FiscalYear fiscalYear = DataExportForm.this.getModelObject().getFiscalYear();
                    final ProcurementPlan procurementPlan =
                            procurementPlanService.findByDepartmentAndFiscalYear(department, fiscalYear);

                    if (procurementPlan == null) {
                        feedbackPanel.error("There is no Procurement Plan for the selected filters!");
                    } else {
                        // initiate the file download
                        download.initiate(target);
                    }

                    target.add(feedbackPanel);
                }

                @Override
                protected void onError(final AjaxRequestTarget target) {
                    super.onError(target);

                    target.add(feedbackPanel);
                    target.add(department);
                    target.add(fiscalYear);
                }
            };
            excelButton.setIconType(FontAwesomeIconType.download);
            add(excelButton);
        }
    }
}
