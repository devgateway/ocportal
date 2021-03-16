package org.devgateway.toolkit.forms.wicket.components.export;

import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.components.form.Select2ChoiceBootstrapFormComponent;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.service.category.DepartmentService;
import org.devgateway.toolkit.persistence.service.category.FiscalYearService;
import org.devgateway.toolkit.persistence.service.excel.DataExportService;
import org.devgateway.toolkit.persistence.service.form.ProcurementPlanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;

/**
 * @author Octavian Ciubotaru
 */
public class GeneralDepartmentReportPanel extends AbstractReportPanel<GeneralDepartmentReportPanel.DataExportBean> {

    protected static final Logger logger = LoggerFactory.getLogger(GeneralDepartmentReportPanel.class);

    @SpringBean
    private DataExportService dataExportService;

    @SpringBean
    private ProcurementPlanService procurementPlanService;

    @SpringBean
    private DepartmentService departmentService;

    @SpringBean
    private FiscalYearService fiscalYearService;

    public static class DataExportBean implements Serializable {

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
        super(id, Model.of(new DataExportBean()));
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        Select2ChoiceBootstrapFormComponent<Department> department =
                ComponentUtil.addSelect2ChoiceField(getDataExportForm(), "department", departmentService);
        department.required();

        Select2ChoiceBootstrapFormComponent<FiscalYear> fiscalYear =
                ComponentUtil.addSelect2ChoiceField(getDataExportForm(), "fiscalYear", fiscalYearService);
        fiscalYear.required();
    }

    @Override
    protected boolean hasData() {
        DataExportBean filters = getDataExportForm().getModelObject();
        final Department department = filters.getDepartment();
        final FiscalYear fiscalYear = filters.getFiscalYear();
        final ProcurementPlan procurementPlan =
                procurementPlanService.findByDepartmentAndFiscalYear(department, fiscalYear);
        return procurementPlan != null;
    }

    protected String getFileName() {
        return "General Department Report.xlsx";
    }

    protected void export(IResource.Attributes attributes) throws IOException {
        DataExportBean filters = getDataExportForm().getModelObject();
        final Department department = filters.getDepartment();
        final FiscalYear fiscalYear = filters.getFiscalYear();

        final byte[] bytes = dataExportService.generateProcurementPlanExcel(
                procurementPlanService.findByDepartmentAndFiscalYear(department, fiscalYear)
                        .getId());

        attributes.getResponse().getOutputStream().write(bytes);
    }
}
