package org.devgateway.toolkit.forms.wicket.components.export;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.persistence.service.excel.AGPOSectionBExporter;

import java.io.IOException;

/**
 * @author Octavian Ciubotaru
 */
public class AGPOSectionBPanel extends AbstractAGPOSectionPanel {

    @SpringBean
    private AGPOSectionBExporter exporter;

    public AGPOSectionBPanel(String id, AjaxFormListener ajaxFormListener) {
        super(id, ajaxFormListener);
    }

    @Override
    protected String getFileName() {
        return "AGPO Section B: All Contracts Awards to the Target Group.xlsx";
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
