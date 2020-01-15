package org.devgateway.ocds.web.rest.controller.excelchart;

import io.swagger.annotations.ApiOperation;
import org.bson.Document;
import org.devgateway.ocds.web.rest.controller.ProjectStatisticsController;
import org.devgateway.ocds.web.rest.controller.request.LangYearFilterPagingRequest;
import org.devgateway.toolkit.web.Constants;
import org.devgateway.toolkit.web.excelcharts.ChartType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author mpostelnicu
 * @since 14/12/2019
 * <p>
 * Exports an excel chart based on *Project Statistics Endpoints*
 */
@RestController
public class ProjectStatisticsExcelController extends ExcelChartOCDSController {
    @Autowired
    private ExcelChartGenerator excelChartGenerator;

    @Autowired
    private ExcelChartHelper excelChartHelper;

    @Autowired
    private ProjectStatisticsController projectStatisticsController;

    @ApiOperation(value = "Exports Number of projects by year (it shows in map widget) excel chart ")
    @RequestMapping(value = "/api/ocds/numberOfProjectsByYearExcelChart", method = {RequestMethod.GET,
            RequestMethod.POST})
    public void numberOfProjectsByYearExcelChart(@ModelAttribute @Valid final LangYearFilterPagingRequest filter,
                                                 final HttpServletResponse response) throws IOException {

        final String chartTitle = translationService.getValue(filter.getLanguage(), "charts:projectCount:title");

        final List<Document> endpointResult =
                projectStatisticsController.numberOfProjectsByYear(filter);

        final List<?> categories = excelChartHelper.getCategoriesFromDBObject(
                getExportYearMonthXAxis(filter), endpointResult);

        final List<List<? extends Number>> values = new ArrayList<>();
        final List<Number> valuesFromDBObject = excelChartHelper.getValuesFromDBObject(endpointResult, categories,
                getExportYearMonthXAxis(filter), ProjectStatisticsController.Keys.COUNT);

        if (!valuesFromDBObject.isEmpty()) {
            values.add(valuesFromDBObject);
        }

        // check if we have anything to display before setting the *seriesTitle*.
        final List<String> seriesTitle;
        if (!values.isEmpty()) {
            seriesTitle = Arrays.asList(
                    translationService.getValue(filter.getLanguage(), "charts:projectCount:xAxisTitle")
            );
        } else {
            seriesTitle = new ArrayList<>();
        }

        response.setContentType(Constants.ContentType.XLSX);
        response.setHeader("Content-Disposition", "attachment; filename=" + chartTitle + ".xlsx");
        response.getOutputStream().write(
                excelChartGenerator.getExcelChart(
                        ChartType.barcol,
                        chartTitle,
                        seriesTitle,
                        categories, values));
    }

    @ApiOperation(value = "Exports total Amount from project planning by year (it shows in map widget) excel chart ")
    @RequestMapping(value = "/api/ocds/amountBudgetedByYearExcelChart", method = {RequestMethod.GET,
            RequestMethod.POST})
    public void amountBudgetedByYearExcelChart(@ModelAttribute @Valid final LangYearFilterPagingRequest filter,
                                               final HttpServletResponse response) throws IOException {

        final String chartTitle = translationService.getValue(filter.getLanguage(), "charts:amountBudgeted:title");

        final List<Document> endpointResult =
                projectStatisticsController.amountBudgetedByYear(filter);

        final List<?> categories = excelChartHelper.getCategoriesFromDBObject(
                getExportYearMonthXAxis(filter), endpointResult);

        final List<List<? extends Number>> values = new ArrayList<>();
        final List<Number> valuesFromDBObject = excelChartHelper.getValuesFromDBObject(endpointResult, categories,
                getExportYearMonthXAxis(filter), ProjectStatisticsController.Keys.AMOUNT);

        if (!valuesFromDBObject.isEmpty()) {
            values.add(valuesFromDBObject);
        }

        // check if we have anything to display before setting the *seriesTitle*.
        final List<String> seriesTitle;
        if (!values.isEmpty()) {
            seriesTitle = Arrays.asList(
                    translationService.getValue(filter.getLanguage(), "charts:amountBudgeted:xAxisTitle")
            );
        } else {
            seriesTitle = new ArrayList<>();
        }

        response.setContentType(Constants.ContentType.XLSX);
        response.setHeader("Content-Disposition", "attachment; filename=" + chartTitle + ".xlsx");
        response.getOutputStream().write(
                excelChartGenerator.getExcelChart(
                        ChartType.barcol,
                        chartTitle,
                        seriesTitle,
                        categories, values));
    }
}
