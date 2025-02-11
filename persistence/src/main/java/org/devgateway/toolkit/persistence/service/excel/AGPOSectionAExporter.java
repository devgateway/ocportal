package org.devgateway.toolkit.persistence.service.excel;

import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
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
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.SetJoin;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author mpostelnicu
 */
@Service
@Transactional
public class AGPOSectionAExporter {

    private static final int TOTAL_PROCUREMENT_SPEND = 0;
    private static final int TOTAL_PROCUREMENT_RESERVED = 1;
    private static final int PERCENT_RESERVED = 2;
    private static final int NO_CONTRACTS_AGPO = 3;
    private static final int VALUE_CONTRACTS_AGPO = 4;
    private static final int PERCENT_CONTRACTS_AGPO = 5;

    @Autowired
    private TenderProcessService tenderProcessService;

    public boolean hasData(NamedDateRange range) {
        return tenderProcessService.count(getSpecification(range, false, false)) > 0;
    }

    private XSSFCellStyle createDefaultNumberCellStyle(XSSFWorkbook workbook) {
        XSSFCellStyle numberCellStyle = workbook.createCellStyle();
        numberCellStyle.setDataFormat(BuiltinFormats.getBuiltinFormat("0.00"));
        return numberCellStyle;
    }

    public XSSFWorkbook export(NamedDateRange range) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();
        sheet.setDefaultColumnWidth(20);
        addHeaderRow(sheet);
        //applyColumnStyles(sheet);
        addValuesRow(sheet, range);

        return workbook;
    }


    private BigDecimal calculateAmountContractSpend(NamedDateRange range) {
        List<TenderProcess> tenderProcesses = tenderProcessService.findAll(getSpecification(range, true, false));
        return sumAllContractValues(tenderProcesses);
    }

    private BigDecimal calculateRangeAmountContractAgpo(NamedDateRange range) {
        List<TenderProcess> tenderProcesses = tenderProcessService.findAll(getSpecification(range, false, true));
        return sumAllContractValues(tenderProcesses);
    }

    private BigDecimal sumAllContractValues(List<TenderProcess> tenderProcesses) {
        return tenderProcesses.stream().flatMap(tp -> tp.getContract().stream())
                .filter(c -> Objects.nonNull(c.getContractValue())).map(Contract::getContractValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Long countAllContracts(List<TenderProcess> tenderProcesses) {
        return tenderProcesses.stream().flatMap(tp -> tp.getContract().stream())
                .filter(c -> Objects.nonNull(c.getContractValue())).count();
    }

    private Long calculateRangeNoContractsAgpo(NamedDateRange range) {
        List<TenderProcess> tenderProcesses = tenderProcessService.findAll(getSpecification(range, false, true));
        return countAllContracts(tenderProcesses);
    }


    private void addHeaderRow(XSSFSheet sheet) {
        XSSFRow hrow = sheet.createRow(0);
        hrow.createCell(TOTAL_PROCUREMENT_SPEND).setCellValue("Total Procurement Spend for the FY");
        hrow.createCell(TOTAL_PROCUREMENT_RESERVED).setCellValue("Total Procurement Value Reserved for the FY");
        hrow.createCell(PERCENT_RESERVED).setCellValue("% Reserved = (D/E)");
        hrow.createCell(NO_CONTRACTS_AGPO).setCellValue("Number of contracts awarded during the 6 months "
                + "reporting period");
        hrow.createCell(VALUE_CONTRACTS_AGPO).setCellValue("Value of contracts awarded during the 6 months "
                + "reporting period");
        hrow.createCell(PERCENT_CONTRACTS_AGPO).setCellValue("% value of contracts awarded = (H/D) x 100");
    }

    private void addValuesRow(XSSFSheet sheet, NamedDateRange range) {

        XSSFRow hrow = sheet.createRow(1);

        hrow.createCell(TOTAL_PROCUREMENT_SPEND, CellType.NUMERIC).
                setCellValue(calculateAmountContractSpend(range).doubleValue());

        hrow.createCell(PERCENT_RESERVED, CellType.NUMERIC).setCellValue(30d);
        hrow.createCell(TOTAL_PROCUREMENT_RESERVED, CellType.FORMULA).setCellFormula("A2*C2/100");
        hrow.createCell(NO_CONTRACTS_AGPO, CellType.NUMERIC).setCellValue(calculateRangeNoContractsAgpo(range));
        hrow.createCell(VALUE_CONTRACTS_AGPO, CellType.NUMERIC)
                .setCellValue(calculateRangeAmountContractAgpo(range).doubleValue());
        hrow.createCell(PERCENT_CONTRACTS_AGPO, CellType.FORMULA).setCellFormula("E2/A2*100");

        applyNumericStyle(hrow);
    }
    
    private void applyNumericStyle(XSSFRow hrow) {
        XSSFCellStyle numberCellStyle = createDefaultNumberCellStyle(hrow.getSheet().getWorkbook());
        for (Cell cell : hrow) {
            cell.setCellStyle(numberCellStyle);
        }
    }

    private Specification<TenderProcess> getSpecification(NamedDateRange range, boolean useFyDates, boolean agpo) {
        return (r, q, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            SetJoin<TenderProcess, Contract> contractJoin = r.join(TenderProcess_.contract);
            predicates.add(cb.equal(contractJoin.get(Contract_.status), DBConstants.Status.APPROVED));

            if (agpo) {
                predicates.add(contractJoin.join(Contract_.targetGroup).get(TargetGroup_.label)
                        .in(DBConstants.TargetGroup.AGPO_CATEGORIES));
            }


            if (range.isSecondInterval() && useFyDates) {
                predicates.add(cb.between(contractJoin.get(Contract_.contractApprovalDate),
                        range.getFiscalYearStartDate(), range.getFiscalYearEndDate()));
            } else {
                predicates.add(cb.between(contractJoin.get(Contract_.contractApprovalDate), range.getStartDate(),
                        range.getEndDate()));
            }

            predicates.add(cb.equal(r.join(TenderProcess_.tender).get(Tender_.status), DBConstants.Status.APPROVED));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
