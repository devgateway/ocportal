package org.devgateway.toolkit.forms.wicket.components.export;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.persistence.service.excel.AGPOSectionAExporter;

import java.io.IOException;

/**
 * @author mpostelnicu
 */
public class AGPOSectionAPanel extends AbstractAGPOSectionPanel {

    @SpringBean
    private AGPOSectionAExporter exporter;

    public AGPOSectionAPanel(String id, AjaxFormListener ajaxFormListener) {
        super(id, ajaxFormListener);
    }

    @Override
    protected String getFileName() {
        return "AGPO Section A: Allocations to the Preference and Reservation.xlsx";
    }

    @Override
    protected boolean hasData() {
        Filter filter = getDataExportForm().getModelObject();
        return filter.getDateRange() != null
                && exporter.hasData(filter.getDateRange());
    }

    @Override
    protected void export(IResource.Attributes attributes) throws IOException {
        Filter filter = getDataExportForm().getModelObject();
        XSSFWorkbook workbook = exporter.export(filter.getDateRange());
        workbook.write(attributes.getResponse().getOutputStream());
    }
}
