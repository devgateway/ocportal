package org.devgateway.ocds.web.rest.controller.excelchart;

/**
 * @author idobre
 * @since 9/14/16
 *
 * @see {@link AbstractExcelControllerTest}
 */
public class TenderPercentagesExcelControllerTest extends AbstractExcelControllerTest {
//    @Autowired
//    private TenderPercentagesExcelController tenderPercentagesExcelController;
//
//    @Test
//    public void cancelledFundingPercentageExcelChart() throws Exception {
//        LangYearFilterPagingRequest filter = getLangYearFilterMockRequest();
//        tenderPercentagesExcelController.cancelledFundingPercentageExcelChart(
//                filter,
//                mockHttpServletResponse);
//
//        final byte[] responseOutput = mockHttpServletResponse.getContentAsByteArray();
//        final Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(responseOutput));
//        Assert.assertNotNull(workbook);
//
//        final Sheet sheet = workbook.getSheet(ChartType.area.toString());
//        Assert.assertNotNull("check chart type, sheet name should be the same as the type", sheet);
//
//        final XSSFDrawing drawing = (XSSFDrawing) sheet.getDrawingPatriarch();
//        final List<XSSFChart> charts =  drawing.getCharts();
//        Assert.assertEquals("number of charts", 1, charts.size());
//
//        final XSSFChart chart = charts.get(0);
//        Assert.assertEquals("chart title",
//                translationService.getValue(filter.getLanguage(),"charts:cancelledPercents:title")
//                , chart.getTitle().getString());
//
//        final List<? extends XSSFChartAxis> axis = chart.getAxis();
//        Assert.assertEquals("number of axis", 2, axis.size());
//
//        final CTChart ctChart = chart.getCTChart();
//        Assert.assertEquals("Check if we have 1 area chart", 1, ctChart.getPlotArea().getAreaChartArray().length);
//    }
//
//    @Test
//    public void numberTendersUsingEBidExcelChart() throws Exception {
//        LangYearFilterPagingRequest filter = getLangYearFilterMockRequest();
//        tenderPercentagesExcelController.numberTendersUsingEBidExcelChart(
//                filter,
//                mockHttpServletResponse);
//
//        final byte[] responseOutput = mockHttpServletResponse.getContentAsByteArray();
//        final Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(responseOutput));
//        Assert.assertNotNull(workbook);
//
//        final Sheet sheet = workbook.getSheet(ChartType.area.toString());
//        Assert.assertNotNull("check chart type, sheet name should be the same as the type", sheet);
//
//        final XSSFDrawing drawing = (XSSFDrawing) sheet.getDrawingPatriarch();
//        final List<XSSFChart> charts =  drawing.getCharts();
//        Assert.assertEquals("number of charts", 1, charts.size());
//
//        final XSSFChart chart = charts.get(0);
//        Assert.assertEquals("chart title",
//                translationService.getValue(filter.getLanguage(), "charts:nrEBid:title")
//                , chart.getTitle().getString());
//
//        final List<? extends XSSFChartAxis> axis = chart.getAxis();
//        Assert.assertEquals("number of axis", 2, axis.size());
//
//        final CTChart ctChart = chart.getCTChart();
//        Assert.assertEquals("Check if we have 1 area chart", 1, ctChart.getPlotArea().getAreaChartArray().length);
//    }
//
//    @Test
//    public void percentTendersUsingEBidExcelChart() throws Exception {
//        LangYearFilterPagingRequest filter = getLangYearFilterMockRequest();
//        tenderPercentagesExcelController.percentTendersUsingEBidExcelChart(
//                filter,
//                mockHttpServletResponse);
//
//        final byte[] responseOutput = mockHttpServletResponse.getContentAsByteArray();
//        final Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(responseOutput));
//        Assert.assertNotNull(workbook);
//
//        final Sheet sheet = workbook.getSheet(ChartType.area.toString());
//        Assert.assertNotNull("check chart type, sheet name should be the same as the type", sheet);
//
//        final XSSFDrawing drawing = (XSSFDrawing) sheet.getDrawingPatriarch();
//        final List<XSSFChart> charts =  drawing.getCharts();
//        Assert.assertEquals("number of charts", 1, charts.size());
//
//        final XSSFChart chart = charts.get(0);
//        Assert.assertEquals("chart title",
//                translationService.getValue(filter.getLanguage(),"charts:percentEBid:title"),
//                chart.getTitle().getString());
//
//        final List<? extends XSSFChartAxis> axis = chart.getAxis();
//        Assert.assertEquals("number of axis", 2, axis.size());
//
//        final CTChart ctChart = chart.getCTChart();
//        Assert.assertEquals("Check if we have 1 area chart", 1, ctChart.getPlotArea().getAreaChartArray().length);
//    }
//
//    @Test
//    public void tendersWithLinkedProcurementPlanExcelChart() throws Exception {
//        LangYearFilterPagingRequest filter = getLangYearFilterMockRequest();
//        tenderPercentagesExcelController.tendersWithLinkedProcurementPlanExcelChart(
//                filter,
//                mockHttpServletResponse);
//
//        final byte[] responseOutput = mockHttpServletResponse.getContentAsByteArray();
//        final Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(responseOutput));
//        Assert.assertNotNull(workbook);
//
//        final Sheet sheet = workbook.getSheet(ChartType.area.toString());
//        Assert.assertNotNull("check chart type, sheet name should be the same as the type", sheet);
//
//        final XSSFDrawing drawing = (XSSFDrawing) sheet.getDrawingPatriarch();
//        final List<XSSFChart> charts =  drawing.getCharts();
//        Assert.assertEquals("number of charts", 1, charts.size());
//
//        final XSSFChart chart = charts.get(0);
//        Assert.assertEquals("chart title",
//                translationService.getValue(filter.getLanguage(), "charts:percentWithTenders:title")
//                , chart.getTitle().getString());
//
//        final List<? extends XSSFChartAxis> axis = chart.getAxis();
//        Assert.assertEquals("number of axis", 2, axis.size());
//
//        final CTChart ctChart = chart.getCTChart();
//        Assert.assertEquals("Check if we have 1 area chart", 1, ctChart.getPlotArea().getAreaChartArray().length);
//    }
}
