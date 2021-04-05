package org.devgateway.toolkit.forms.wicket.components.export;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.ocds.persistence.mongo.Tender;
import org.devgateway.ocds.persistence.mongo.constants.MongoConstants;
import org.devgateway.toolkit.forms.wicket.components.util.ComponentUtil;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.service.excel.DirectProcurementsAboveExporter;

import java.io.IOException;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Octavian Ciubotaru
 */
public class DirectProcurementsAboveReportPanel extends AbstractReportPanel<DirectProcurementsAboveReportPanel.Filter> {

    private static final Set<String> DIRECT_PROCUREMENT_METHODS =
            MongoConstants.PROCUREMENT_METHOD_MAP.entrySet().stream()
                    .filter(e -> e.getValue() == Tender.ProcurementMethod.direct)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toSet());

    @SpringBean
    private DirectProcurementsAboveExporter exporter;

    public static class Filter implements Serializable  {

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

    public DirectProcurementsAboveReportPanel(String id, AjaxFormListener ajaxFormListener) {
        super(id, ajaxFormListener, Model.of(new Filter()));
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        ComponentUtil.addDateField(getDataExportForm(), "startDate");

        ComponentUtil.addDateField(getDataExportForm(), "endDate");
    }

    protected Component newTitle(String id) {
        return new Label(id, new StringResourceModel("title")
                .setParameters(DBConstants.Reports.DIRECT_PROCUREMENT_THRESHOLD));
    }

    @Override
    protected String getFileName() {
        return MessageFormat.format("Direct Procurements Above Ksh {0,number}.xlsx",
                DBConstants.Reports.DIRECT_PROCUREMENT_THRESHOLD);
    }

    @Override
    protected boolean hasData() {
        Filter filter = getDataExportForm().getModelObject();
        return exporter.hasData(filter.getStartDate(), filter.getEndDate(), DIRECT_PROCUREMENT_METHODS);
    }

    @Override
    protected void export(IResource.Attributes attributes) throws IOException {
        Filter filter = getDataExportForm().getModelObject();
        XSSFWorkbook workbook = exporter.export(filter.getStartDate(), filter.getEndDate(), DIRECT_PROCUREMENT_METHODS);
        workbook.write(attributes.getResponse().getOutputStream());
    }
}
