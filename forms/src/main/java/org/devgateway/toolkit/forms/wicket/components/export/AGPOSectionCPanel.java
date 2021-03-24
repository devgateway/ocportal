package org.devgateway.toolkit.forms.wicket.components.export;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.persistence.service.excel.AGPOSectionCExporter;

import java.io.IOException;

/**
 * @author Octavian Ciubotaru
 */
public class AGPOSectionCPanel extends AbstractAGPOSectionPanel {

    @SpringBean
    private AGPOSectionCExporter exporter;

    public AGPOSectionCPanel(String id, AjaxFormListener ajaxFormListener) {
        super(id, ajaxFormListener);
    }

    @Override
    protected String getFileName() {
        return "AGPO Section C: Summary of All contract Awards as presented in Section B.xlsx";
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
