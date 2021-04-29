package org.devgateway.ocds.web.rest.controller.excelchart;

/**
 * @author idobre
 * @since 9/14/16
 *
 * @see {@link AbstractExcelControllerTest}
 */
public class AverageNumberOfTenderersExcelControllerTest extends AbstractExcelControllerTest {
//    @Autowired
//    private AverageNumberOfTenderersExcelController averageNumberOfTenderersExcelController;
//
//    @Test
//    public void averageNumberBidsExcelChart() throws Exception {
//        LangYearFilterPagingRequest filter = getLangYearFilterMockRequest();
//        averageNumberOfTenderersExcelController.averageNumberBidsExcelChart(
//                filter,
//                mockHttpServletResponse);
//
//        final byte[] responseOutput = mockHttpServletResponse.getContentAsByteArray();
//        final Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(responseOutput));
//        Assert.assertNotNull(workbook);
//
//        final Sheet sheet = workbook.getSheet(ChartType.barcol.toString());
//        Assert.assertNotNull("check chart type, sheet name should be the same as the type", sheet);
//
//        final XSSFDrawing drawing = (XSSFDrawing) sheet.getDrawingPatriarch();
//        final List<XSSFChart> charts =  drawing.getCharts();
//        Assert.assertEquals("number of charts", 1, charts.size());
//
//        final XSSFChart chart = charts.get(0);
//        Assert.assertEquals("chart title",
//            translationService.getValue(filter.getLanguage(),"charts:avgNrBids:title"),
//                chart.getTitle().getString());
//
//        final List<? extends XSSFChartAxis> axis = chart.getAxis();
//        Assert.assertEquals("number of axis", 2, axis.size());
//
//        final CTChart ctChart = chart.getCTChart();
//        Assert.assertEquals("Check if we have 1 bar chart", 1, ctChart.getPlotArea().getBarChartArray().length);
//    }
}
