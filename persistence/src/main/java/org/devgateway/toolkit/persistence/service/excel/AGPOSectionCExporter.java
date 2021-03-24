package org.devgateway.toolkit.persistence.service.excel;

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

import javax.persistence.criteria.SetJoin;
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

        XSSFCellStyle numberCellStyle = workbook.createCellStyle();
        numberCellStyle.setDataFormat(BuiltinFormats.getBuiltinFormat("0.00"));

        XSSFCellStyle intCellStyle = workbook.createCellStyle();
        intCellStyle.setDataFormat(BuiltinFormats.getBuiltinFormat("0"));

        XSSFCellStyle percCellStyle = workbook.createCellStyle();
        percCellStyle.setDataFormat(BuiltinFormats.getBuiltinFormat("0.00%"));

        XSSFSheet sheet = workbook.createSheet();
        sheet.setDefaultColumnWidth(35);
        sheet.setDefaultColumnStyle(NUM_CONTRACTS, intCellStyle);
        sheet.setDefaultColumnStyle(VALUE_OF_CONTRACTS, numberCellStyle);
        sheet.setDefaultColumnStyle(PERCENT, percCellStyle);

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
        List<TenderProcess> tenderProcesses = tenderProcessService.findAll(getSpecification(range));

        Map<String, Summary> grouped = tenderProcesses.stream().collect(Collectors.groupingBy(
                tp -> tp.getSingleContract().getTargetGroup().getLabel(),
                Collectors.reducing(
                        new Summary(0, BigDecimal.ZERO),
                        tp -> new Summary(1, tp.getSingleContract().getContractValue()),
                        Summary::add)));

        int numCats = grouped.size();

        grouped.forEach((agpoCategory, summary) -> addCategoryRow(sheet, numCats, agpoCategory, summary));

        addTotalRow(sheet);
    }

    private void addCategoryRow(XSSFSheet sheet, int numCats, String agpoCategory, Summary summary) {
        XSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
        row.createCell(CATEGORY).setCellValue(agpoCategory);
        row.createCell(NUM_CONTRACTS).setCellValue(summary.count);
        row.createCell(VALUE_OF_CONTRACTS).setCellValue(summary.amount.doubleValue());
        row.createCell(PERCENT).setCellFormula(String.format("C%d/C%d", row.getRowNum() + 1, numCats + 2));
    }

    private void addTotalRow(XSSFSheet sheet) {
        XSSFRow totalRow = sheet.createRow(sheet.getLastRowNum() + 1);
        totalRow.createCell(CATEGORY).setCellValue("Total");
        totalRow.createCell(NUM_CONTRACTS).setCellFormula(String.format("SUM(B%d:B%d)", 2, totalRow.getRowNum()));
        totalRow.createCell(VALUE_OF_CONTRACTS).setCellFormula(String.format("SUM(C%d:C%d)", 2, totalRow.getRowNum()));
        totalRow.createCell(PERCENT).setCellValue(1);
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
