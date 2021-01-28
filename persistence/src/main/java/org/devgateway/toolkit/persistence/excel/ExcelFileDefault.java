package org.devgateway.toolkit.persistence.excel;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.devgateway.toolkit.persistence.excel.service.TranslateService;
import org.devgateway.toolkit.persistence.fm.service.DgFmService;

import java.util.List;

/**
 * Default implementation of the {@link ExcelFile} type.
 *
 * @author idobre
 * @since 13/11/2017
 */
public class ExcelFileDefault implements ExcelFile {
    private final List<Object> objects;

    private final Workbook workbook;

    private final TranslateService translateService;

    private final DgFmService fmService;

    public ExcelFileDefault(final List<Object> objects, final TranslateService translateService,
            final DgFmService fmService) {
        this.fmService = fmService;
        Validate.notNull(objects, "The list of objects can't be null!");
        Validate.noNullElements(objects, "The list of objects can't contain null elements!");

        this.objects = objects;
        this.translateService = translateService;

        // create the excel file
        this.workbook = new SXSSFWorkbook(100);
    }

    public ExcelFileDefault(final List<Object> objects, final DgFmService fmService) {
        this(objects, null, fmService);
    }

    @Override
    public Workbook createWorkbook() {
        // don't do anything if the list of objects is empty, just display the error message.
        if (objects.isEmpty()) {
            final ExcelSheet excelSheet = new ExcelSheetDefault(workbook, translateService, fmService, "no data");
            excelSheet.emptySheet();
        } else {
            final Class clazz = this.objects.get(0).getClass();
            String sheetName = getSheetNameFor(clazz);
            final ExcelSheet excelSheet = new ExcelSheetDefault(workbook, translateService, fmService, sheetName);
            excelSheet.writeSheet(clazz, objects);
        }

        return workbook;
    }

    /**
     * Generate sheet name based on the title of the form.
     */
    private String getSheetNameFor(Class clazz) {
        String sheetName = translateService == null ? null : translateService.getTranslation(clazz);
        if (StringUtils.isEmpty(sheetName)) {
            sheetName = clazz.getSimpleName().toLowerCase();
        }
        return sheetName;
    }
}
