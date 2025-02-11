package org.devgateway.ocds.web.rest.controller.excelchart;

import io.swagger.v3.oas.annotations.Operation;
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

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
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

    @Operation(summary = "Exports Number of projects by year (it shows in map widget) excel chart ")
    @RequestMapping(value = "/api/ocds/numberOfProjectsByYearExcelChart", method = {RequestMethod.GET,
            RequestMethod.POST})
    public void numberOfProjectsByYearExcelChart(@ModelAttribute @Valid final LangYearFilterPagingRequest filter,
                                                 final HttpServletResponse response) throws IOException {

        final String chartTitle = translationService.getValue(filter.getLanguage(), "charts:projectCount:title");
        String xAxisTitle = translationService.getValue(filter.getLanguage(), "charts:projectCount:xAxisTitle");
        String valKey = ProjectStatisticsController.Keys.COUNT;

        final List<Document> endpointResult =
                projectStatisticsController.numberOfProjectsByYear(filter);

        respondWithExcel(filter, response, chartTitle, xAxisTitle, valKey, endpointResult);
    }

    @Operation(summary = "Exports total Amount from project planning by year (it shows in map widget) excel chart ")
    @RequestMapping(value = "/api/ocds/amountBudgetedByYearExcelChart", method = {RequestMethod.GET,
            RequestMethod.POST})
    public void amountBudgetedByYearExcelChart(@ModelAttribute @Valid final LangYearFilterPagingRequest filter,
                                               final HttpServletResponse response) throws IOException {

        final String chartTitle = translationService.getValue(filter.getLanguage(), "charts:amountBudgeted:title");
        String xAxisTitle = translationService.getValue(filter.getLanguage(), "charts:amountBudgeted:xAxisTitle");
        String valKey = ProjectStatisticsController.Keys.AMOUNT;

        final List<Document> endpointResult =
                projectStatisticsController.amountBudgetedByYear(filter);

        respondWithExcel(filter, response, chartTitle, xAxisTitle, valKey, endpointResult);
    }

    @Operation(summary = "Exports Number of contracts by year (it shows in map widget) excel chart ")
    @RequestMapping(value = "/api/ocds/numberOfContractsByYearExcelChart", method = {RequestMethod.GET,
            RequestMethod.POST})
    public void numberOfContractsByYearExcelChart(@ModelAttribute @Valid final LangYearFilterPagingRequest filter,
            final HttpServletResponse response) throws IOException {

        final String chartTitle = translationService.getValue(filter.getLanguage(), "charts:contractCount:title");
        String xAxisTitle = translationService.getValue(filter.getLanguage(), "charts:contractCount:xAxisTitle");
        String valKey = ProjectStatisticsController.Keys.COUNT;

        final List<Document> endpointResult =
                projectStatisticsController.numberOfContractsByYear(filter);

        respondWithExcel(filter, response, chartTitle, xAxisTitle, valKey, endpointResult);
    }

    @Operation(summary = "Exports total Amount from contracts by year (it shows in map widget) excel chart ")
    @RequestMapping(value = "/api/ocds/amountContractedByYearExcelChart", method = {RequestMethod.GET,
            RequestMethod.POST})
    public void amountContractedByYearExcelChart(@ModelAttribute @Valid final LangYearFilterPagingRequest filter,
            final HttpServletResponse response) throws IOException {

        final String chartTitle = translationService.getValue(filter.getLanguage(), "charts:amountContracted:title");
        String xAxisTitle = translationService.getValue(filter.getLanguage(), "charts:amountContracted:xAxisTitle");
        String valKey = ProjectStatisticsController.Keys.AMOUNT;

        final List<Document> endpointResult =
                projectStatisticsController.amountContractedByYear(filter);

        respondWithExcel(filter, response, chartTitle, xAxisTitle, valKey, endpointResult);
    }

    private void respondWithExcel(
            LangYearFilterPagingRequest filter,
            HttpServletResponse response, String chartTitle, String xAxisTitle, String valKey,
            List<Document> endpointResult) throws IOException {
        final List<?> categories = excelChartHelper.getCategoriesFromDBObject(
                getExportYearMonthXAxis(filter), endpointResult);

        final List<List<? extends Number>> values = new ArrayList<>();
        final List<Number> valuesFromDBObject = excelChartHelper.getValuesFromDBObject(endpointResult, categories,
                getExportYearMonthXAxis(filter), valKey);

        if (!valuesFromDBObject.isEmpty()) {
            values.add(valuesFromDBObject);
        }

        // check if we have anything to display before setting the *seriesTitle*.
        final List<String> seriesTitle;
        if (!values.isEmpty()) {
            seriesTitle = Arrays.asList(xAxisTitle);
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
