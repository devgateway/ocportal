package org.devgateway.toolkit.persistence.service.excel;

import static org.devgateway.toolkit.persistence.service.excel.ExporterUtil.createCell;

import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.categories.TargetGroup_;
import org.devgateway.toolkit.persistence.dao.form.Contract;
import org.devgateway.toolkit.persistence.dao.form.Contract_;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess_;
import org.devgateway.toolkit.persistence.dao.form.Tender_;
import org.devgateway.toolkit.persistence.dto.NamedDateRange;
import org.devgateway.toolkit.persistence.service.form.TenderProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.SetJoin;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Octavian Ciubotaru
 */
@Service
public class AGPOSectionCExporter {

    private static final int CATEGORY = 0;
    private static final int NUM_CONTRACTS = 1;
    private static final int VALUE_OF_CONTRACTS = 2;
    private static final int PERCENT = 3;

    @Autowired
    private TenderProcessService tenderProcessService;

    public boolean hasData(NamedDateRange range) {
        return tenderProcessService.count(getSpecification(range)) > 0;
    }

    public XSSFWorkbook export(NamedDateRange range) {
        XSSFWorkbook workbook = new XSSFWorkbook();

        XSSFSheet sheet = workbook.createSheet();
        sheet.setDefaultColumnWidth(35);

        addHeaderRow(sheet);

        addBody(sheet, range);

        return workbook;
    }

    private void addHeaderRow(XSSFSheet sheet) {
        XSSFRow row = sheet.createRow(0);
        row.createCell(CATEGORY).setCellValue("Category");
        row.createCell(NUM_CONTRACTS).setCellValue("No. of Contracts Awarded");
        row.createCell(VALUE_OF_CONTRACTS).setCellValue("Total Value of Contracts Awarded");
        row.createCell(PERCENT).setCellValue("% of contract value per category");
    }

    private static class Summary {

        private final int count;

        private final BigDecimal amount;

        Summary(int count, BigDecimal amount) {
            this.count = count;
            this.amount = amount;
        }

        public static Summary add(Summary a, Summary b) {
            return new Summary(a.count + b.count, a.amount.add(b.amount));
        }
    }

    private void addBody(XSSFSheet sheet, NamedDateRange range) {
        XSSFWorkbook workbook = sheet.getWorkbook();

        XSSFCellStyle numberCellStyle = workbook.createCellStyle();
        numberCellStyle.setDataFormat(BuiltinFormats.getBuiltinFormat("0.00"));

        XSSFCellStyle intCellStyle = workbook.createCellStyle();
        intCellStyle.setDataFormat(BuiltinFormats.getBuiltinFormat("0"));

        XSSFCellStyle percCellStyle = workbook.createCellStyle();
        percCellStyle.setDataFormat(BuiltinFormats.getBuiltinFormat("0.00%"));

        List<TenderProcess> tenderProcesses = tenderProcessService.findAll(getSpecification(range));

        Map<String, Summary> grouped = tenderProcesses.stream().collect(Collectors.groupingBy(
                tp -> tp.getSingleContract().getTargetGroup().getLabel(),
                Collectors.reducing(
                        new Summary(0, BigDecimal.ZERO),
                        tp -> new Summary(1, tp.getSingleContract().getContractValue()),
                        Summary::add)));

        int numCats = grouped.size();

        grouped.forEach((agpoCategory, summary) ->
                addCategoryRow(sheet, numCats, agpoCategory, summary, numberCellStyle, intCellStyle, percCellStyle));

        addTotalRow(sheet, numberCellStyle, intCellStyle, percCellStyle);
    }

    private void addCategoryRow(XSSFSheet sheet, int numCats, String agpoCategory, Summary summary,
            XSSFCellStyle numberCellStyle, XSSFCellStyle intCellStyle, XSSFCellStyle percCellStyle) {
        XSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);

        row.createCell(CATEGORY).setCellValue(agpoCategory);

        createCell(row, NUM_CONTRACTS, intCellStyle).setCellValue(summary.count);

        createCell(row, VALUE_OF_CONTRACTS, numberCellStyle).setCellValue(summary.amount.doubleValue());

        String formula = String.format("C%d/C%d", row.getRowNum() + 1, numCats + 2);
        createCell(row, PERCENT, percCellStyle).setCellFormula(formula);
    }

    private void addTotalRow(XSSFSheet sheet,
            XSSFCellStyle numberCellStyle, XSSFCellStyle intCellStyle, XSSFCellStyle percCellStyle) {
        XSSFRow totalRow = sheet.createRow(sheet.getLastRowNum() + 1);

        totalRow.createCell(CATEGORY).setCellValue("Total");

        String numFormula = String.format("SUM(B%d:B%d)", 2, totalRow.getRowNum());
        createCell(totalRow, NUM_CONTRACTS, intCellStyle).setCellFormula(numFormula);

        String valueFormula = String.format("SUM(C%d:C%d)", 2, totalRow.getRowNum());
        createCell(totalRow, VALUE_OF_CONTRACTS, numberCellStyle).setCellFormula(valueFormula);

        createCell(totalRow, PERCENT, percCellStyle).setCellValue(1);
    }

    private Specification<TenderProcess> getSpecification(NamedDateRange range) {
        return (r, q, cb) -> {
            SetJoin<TenderProcess, Contract> contractJoin = r.join(TenderProcess_.contract);
            return cb.and(
                    cb.equal(r.join(TenderProcess_.tender).get(Tender_.status), DBConstants.Status.APPROVED),
                    cb.equal(contractJoin.get(Contract_.status), DBConstants.Status.APPROVED),
                    cb.between(contractJoin.get(Contract_.contractApprovalDate),
                            range.getStartDate(), range.getEndDate()),
                    contractJoin.join(Contract_.targetGroup).get(TargetGroup_.label)
                            .in(DBConstants.TargetGroup.AGPO_CATEGORIES));
        };
    }
}
