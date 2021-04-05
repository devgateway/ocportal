package org.devgateway.toolkit.forms.wicket.components.export;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.persistence.service.excel.AGPOContractsExporter;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Octavian Ciubotaru
 */
public class AGPOContractsReportPanel extends AbstractReportPanel<AGPOContractsReportPanel.Filter> {

    public static class Filter implements Serializable {

        private Date startDate;
        private Date endDate;

        public Date getStartDate() {
            return startDate;
        }

        public void setStartDate(Date startDate) {
            this.startDate = startDate;
        }

        public Date getEndDate() {
            return endDate;
        }

        public void setEndDate(Date endDate) {
            this.endDate = endDate;
        }
    }

    @SpringBean
    private AGPOContractsExporter exporter;

    public AGPOContractsReportPanel(String id, AjaxFormListener ajaxFormListener) {
        super(id, ajaxFormListener, Model.of(new Filter()));
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        ComponentUtil.addDateField(getDataExportForm(), "startDate");

        ComponentUtil.addDateField(getDataExportForm(), "endDate");
    }

    @Override
    protected String getFileName() {
        return "All Contract Awards under the Preferences and Reservation Scheme.xlsx";
    }

    @Override
    protected boolean hasData() {
        Filter filter = getDataExportForm().getModelObject();
        return exporter.hasData(filter.getStartDate(), filter.getEndDate());
    }

    @Override
    protected void export(IResource.Attributes attributes) throws IOException {
        Filter filter = getDataExportForm().getModelObject();
        XSSFWorkbook workbook = exporter.export(filter.getStartDate(), filter.getEndDate());
        workbook.write(attributes.getResponse().getOutputStream());
    }
}
