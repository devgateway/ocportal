package org.devgateway.toolkit.forms.wicket.components.export;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.core.markup.html.bootstrap.form.BootstrapForm;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeIconType;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.ladda.LaddaAjaxButton;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.IRequestCycle;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.components.form.AJAXDownload;
import org.devgateway.toolkit.forms.wicket.components.form.GenericBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.form.Select2ChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.forms.wicket.visitors.GenericBootstrapValidationVisitor;
import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.service.category.DepartmentService;
import org.devgateway.toolkit.persistence.service.category.FiscalYearService;
import org.devgateway.toolkit.persistence.service.excel.DataExportService;
import org.devgateway.toolkit.persistence.service.form.ProcurementPlanService;
import org.devgateway.toolkit.web.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * @author Octavian Ciubotaru
 */
public class GeneralDepartmentReportPanel extends Panel {

    protected static final Logger logger = LoggerFactory.getLogger(GeneralDepartmentReportPanel.class);

    @SpringBean
    private DataExportService dataExportService;

    @SpringBean
    private ProcurementPlanService procurementPlanService;

    @SpringBean
    private DepartmentService departmentService;

    @SpringBean
    private FiscalYearService fiscalYearService;

    private static class DataExportBean implements Serializable {

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

    public GeneralDepartmentReportPanel(String id) {
        super(id);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        add(new Label("title", new StringResourceModel("title")));

        final BootstrapForm<DataExportBean> dataExportForm = new BootstrapForm<>("dataExportForm",
                new CompoundPropertyModel<>(new DataExportBean()));
        add(dataExportForm);

        Select2ChoiceBootstrapFormComponent<Department> department =
                ComponentUtil.addSelect2ChoiceField(dataExportForm, "department", departmentService);
        department.required();

        Select2ChoiceBootstrapFormComponent<FiscalYear> fiscalYear =
                ComponentUtil.addSelect2ChoiceField(dataExportForm, "fiscalYear", fiscalYearService);
        fiscalYear.required();

        final AJAXDownload download = new AJAXDownload() {
            @Override
            protected IRequestHandler getHandler() {
                return new IRequestHandler() {
                    @Override
                    public void respond(final IRequestCycle requestCycle) {
                        final HttpServletResponse response = (HttpServletResponse) requestCycle
                                .getResponse().getContainerResponse();

                        try {
                            final Department department = dataExportForm.getModelObject().getDepartment();
                            final FiscalYear fiscalYear = dataExportForm.getModelObject().getFiscalYear();

                            final byte[] bytes = dataExportService.generateProcurementPlanExcel(
                                    procurementPlanService.findByDepartmentAndFiscalYear(department, fiscalYear)
                                            .getId());

                            response.setContentType(
                                    Constants.ContentType.XLSX);
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
        dataExportForm.add(download);

        final LaddaAjaxButton excelButton = new LaddaAjaxButton("excelButton",
                new StringResourceModel("export", this),
                Buttons.Type.Success) {
            @Override
            protected void onConfigure() {
                super.onConfigure();
                setDefaultFormProcessing(true);
            }

            @Override
            protected void onSubmit(final AjaxRequestTarget target) {
                super.onSubmit(target);

                final Department department = dataExportForm.getModelObject().getDepartment();
                final FiscalYear fiscalYear = dataExportForm.getModelObject().getFiscalYear();
                final ProcurementPlan procurementPlan =
                        procurementPlanService.findByDepartmentAndFiscalYear(department, fiscalYear);

                if (procurementPlan == null) {
                    dataExportForm.error(getString("procurementPlan.notFound"));
                } else {
                    // initiate the file download
                    download.initiate(target);
                }

                GeneralDepartmentReportPanel.this.onSubmit(target);
            }

            @Override
            protected void onError(final AjaxRequestTarget target) {
                super.onError(target);

                dataExportForm.visitChildren(GenericBootstrapFormComponent.class,
                        new GenericBootstrapValidationVisitor(target));

                GeneralDepartmentReportPanel.this.onError(target);
            }
        };
        excelButton.setIconType(FontAwesomeIconType.download);
        dataExportForm.add(excelButton);
    }

    public void onSubmit(final AjaxRequestTarget target) {
    }

    public void onError(final AjaxRequestTarget target) {
    }
}
