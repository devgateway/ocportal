package org.devgateway.toolkit.forms.wicket.page.lists.form.prequalification;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.Buttons;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeIconType;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.ladda.LaddaAjaxLink;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.devgateway.toolkit.forms.wicket.components.form.AJAXDownload;
import org.devgateway.toolkit.forms.wicket.components.table.SelectFilteredBootstrapPropertyColumn;
import org.devgateway.toolkit.forms.wicket.components.table.TextFilteredBootstrapPropertyColumn;
import org.devgateway.toolkit.forms.wicket.page.edit.form.prequalification.EditPrequalificationYearRangePage;
import org.devgateway.toolkit.forms.wicket.page.lists.AbstractListPage;
import org.devgateway.toolkit.forms.wicket.providers.GenericPersistableJpaTextChoiceProvider;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationSchema;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationYearRange;
import org.devgateway.toolkit.persistence.service.filterstate.JpaFilterState;
import org.devgateway.toolkit.persistence.service.filterstate.prequalification.PrequalificationYearRangeFilterState;
import org.devgateway.toolkit.persistence.service.prequalification.PrequalificationSchemaService;
import org.devgateway.toolkit.persistence.service.prequalification.PrequalificationYearRangeService;
import org.devgateway.toolkit.web.Constants;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.wicketstuff.annotation.mount.MountPath;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.devgateway.toolkit.persistence.service.prequalification.PrequalificationSchemaService.selectableSpecification;

@AuthorizeInstantiation(SecurityConstants.Roles.ROLE_ADMIN)
@MountPath
public class ListPrequalificationYearRangePage extends AbstractListPage<PrequalificationYearRange> {

    @SpringBean
    private PrequalificationYearRangeService prequalificationYearRangeService;

    @SpringBean
    private PrequalificationSchemaService prequalificationSchemaService;

    public ListPrequalificationYearRangePage(PageParameters parameters) {
        super(parameters);
        this.jpaService = prequalificationYearRangeService;
        this.editPageClass = EditPrequalificationYearRangePage.class;
        filterGoReset = true;
    }

    @Override
    protected void addColumns() {
        addFmColumn("name", new TextFilteredBootstrapPropertyColumn<>(
                new Model<>((new StringResourceModel("name",
                        ListPrequalificationYearRangePage.this)).getString()),
                "name", "name"));
        addFmColumn("startYear", new PropertyColumn<>(new Model<>(
                (new StringResourceModel("startYear", ListPrequalificationYearRangePage.this
                )).getString()), "startYear", "startYear"));
        addFmColumn("endYear", new PropertyColumn<>(new Model<>(
                (new StringResourceModel("endYear", ListPrequalificationYearRangePage.this
                )).getString()), "endYear", "endYear"));

        GenericPersistableJpaTextChoiceProvider<PrequalificationSchema> schemaProvider =
                new GenericPersistableJpaTextChoiceProvider<>(prequalificationSchemaService);
        schemaProvider.setSpecification(selectableSpecification());
        addFmColumn("schema", new SelectFilteredBootstrapPropertyColumn<>(
                new StringResourceModel("schema", this),
                "schema", "schema", schemaProvider, getDataTable()
        ));
    }

    public class PrequalificationYearRangeActionPanel extends ActionPanel {

        /**
         * @param id
         * @param model
         */
        public PrequalificationYearRangeActionPanel(String id, IModel<PrequalificationYearRange> model) {
            super(id, model);

            PrequalificationYearRange entity = (PrequalificationYearRange) this.getDefaultModelObject();
            AJAXDownload ajaxDownload = exportToExcelAJAX(entity.getId());

            LaddaAjaxLink<Void> downloadExcel = new LaddaAjaxLink<Void>("downloadExcel", Buttons.Type.Warning) {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    ajaxDownload.initiate(target);
                }
            };
            downloadExcel.setIconType(FontAwesomeIconType.file_excel_o);
            downloadExcel.setSize(Buttons.Size.Small);
            downloadExcel.setLabel(new ResourceModel("download"));
            add(ajaxDownload);
            add(downloadExcel);
        }
    }

    @Override
    public ActionPanel getActionPanel(String id, IModel<PrequalificationYearRange> model) {
        return new PrequalificationYearRangeActionPanel(id, model);
    }

    private AJAXDownload exportToExcelAJAX(Long entityId) {
        return new AJAXDownload() {
            @Override
            protected IRequestHandler getHandler() {
                return requestCycle -> {
                    final HttpServletResponse response = (HttpServletResponse) requestCycle
                            .getResponse().getContainerResponse();

                    XSSFWorkbook workbook = new XSSFWorkbook();

                    XSSFCellStyle headerStyle = workbook.createCellStyle();
                    final Font headerFont = workbook.createFont();
                    headerFont.setBold(true);
                    headerStyle.setFont(headerFont);

                    XSSFSheet sheet = workbook.createSheet("categories");
                    AtomicInteger rowCount = new AtomicInteger();
                    Row headerRow = sheet.createRow(rowCount.getAndIncrement());
                    Cell prequalificationNoH = headerRow.createCell(0);
                    prequalificationNoH.setCellType(CellType.STRING);
                    prequalificationNoH.setCellValue(getString("prequalificationNo"));
                    prequalificationNoH.setCellStyle(headerStyle);
                    Cell itemDescriptionH = headerRow.createCell(1);
                    itemDescriptionH.setCellType(CellType.STRING);
                    itemDescriptionH.setCellStyle(headerStyle);
                    itemDescriptionH.setCellValue(getString("itemDescription"));
                    Cell targetGroupH = headerRow.createCell(2);
                    targetGroupH.setCellType(CellType.STRING);
                    targetGroupH.setCellStyle(headerStyle);
                    targetGroupH.setCellValue(getString("targetGroup"));

                    Optional<PrequalificationYearRange> byId = prequalificationYearRangeService.findById(entityId);
                    if (byId.isPresent()) {
                        byId.get().getSchema().getItems().forEach(i -> {
                            Row r = sheet.createRow(rowCount.getAndIncrement());
                            Cell prequalificationNo = r.createCell(0);
                            prequalificationNo.setCellType(CellType.STRING);
                            prequalificationNo.setCellValue(i.toString(byId.get()));
                            Cell itemDescription = r.createCell(1);
                            itemDescription.setCellType(CellType.STRING);
                            itemDescription.setCellValue(i.getName());
                            Cell targetGroup = r.createCell(2);
                            targetGroup.setCellType(CellType.STRING);
                            targetGroup.setCellValue(i.getCompanyCategories().toString());
                        });
                        for (int x = 0; x <= 2; x++) {
                            sheet.autoSizeColumn(x);
                        }
                    } else {
                        throw new RuntimeException("Cannot find the entity with id " + entityId);
                    }

                    response.setContentType(
                            Constants.ContentType.XLSX);
                    response.setHeader("Content-Disposition", "attachment; "
                            + "filename=items-year-range-" + byId.get().getName().replace(' ', '-') + ".xlsx");
                    try {
                        workbook.write(response.getOutputStream());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                };
            }
        };

    }

    @Override
    protected void onInitialize() {
        //attachFm("prequalificationSchemaList");
        super.onInitialize();
    }

    @Override
    public JpaFilterState<PrequalificationYearRange> newFilterState() {
        return new PrequalificationYearRangeFilterState();
    }
}
