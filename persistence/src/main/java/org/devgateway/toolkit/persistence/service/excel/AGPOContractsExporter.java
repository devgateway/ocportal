package org.devgateway.toolkit.persistence.service.excel;

import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.categories.ContractDocumentType;
import org.devgateway.toolkit.persistence.dao.categories.Supplier;
import org.devgateway.toolkit.persistence.dao.categories.TargetGroup;
import org.devgateway.toolkit.persistence.dao.categories.TargetGroup_;
import org.devgateway.toolkit.persistence.dao.form.Contract;
import org.devgateway.toolkit.persistence.dao.form.ContractDocument;
import org.devgateway.toolkit.persistence.dao.form.Contract_;
import org.devgateway.toolkit.persistence.dao.form.Tender;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess_;
import org.devgateway.toolkit.persistence.dao.form.Tender_;
import org.devgateway.toolkit.persistence.dao.prequalification.AbstractContact;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalifiedSupplier;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalifiedSupplierItem;
import org.devgateway.toolkit.persistence.service.form.TenderProcessService;
import org.devgateway.toolkit.persistence.service.prequalification.PrequalifiedSupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.SetJoin;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Octavian Ciubotaru
 */
@Service
public class AGPOContractsExporter {

    private static final int COMPANY_NAME = 0;
    private static final int DIRECTORS = 1;
    private static final int SCHEME = 2;
    private static final int CATEGORY = 3;
    private static final int TENDER_DESC = 4;
    private static final int TENDER_NO = 5;
    private static final int CONTRACT_TYPE = 6;
    private static final int PROCUREMENT_METHOD = 7;
    private static final int CONTRACT_NO = 8;
    private static final int CONTRACT_VALUE = 9;

    @Autowired
    private TenderProcessService tenderProcessService;

    @Autowired
    private PrequalifiedSupplierService prequalifiedSupplierService;


    public boolean hasData(Date from, Date to) {
        return tenderProcessService.count(getSpecification(from, to)) > 0;
    }

    public XSSFWorkbook export(Date from, Date to) {
        List<TenderProcess> tenderProcesses = tenderProcessService.findAll(getSpecification(from, to));

        XSSFWorkbook workbook = new XSSFWorkbook();

        XSSFSheet sheet = workbook.createSheet();

        sheet.setDefaultColumnWidth(20);

        XSSFCellStyle numberCellStyle = workbook.createCellStyle();
        numberCellStyle.setDataFormat(BuiltinFormats.getBuiltinFormat("0.00"));
        sheet.setDefaultColumnStyle(CONTRACT_VALUE, numberCellStyle);

        addHeaderRow(sheet);

        int rowNum = 1;
        for (TenderProcess tenderProcess : tenderProcesses) {
            XSSFRow row = sheet.createRow(rowNum++);
            writeRow(row, tenderProcess);
        }

        addTotalRow(sheet, rowNum);

        return workbook;
    }

    private void addTotalRow(XSSFSheet sheet, int rowNum) {
        XSSFRow totalRow = sheet.createRow(rowNum);
        totalRow.createCell(0).setCellValue("Grand Total");
        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, CONTRACT_VALUE - 1));
        totalRow.createCell(CONTRACT_VALUE).setCellFormula(String.format("SUM(J2:J%s)", rowNum));
    }

    private void addHeaderRow(XSSFSheet sheet) {
        XSSFRow hrow = sheet.createRow(0);
        hrow.createCell(COMPANY_NAME).setCellValue("Company Name");
        hrow.createCell(DIRECTORS).setCellValue("Directors");
        hrow.createCell(SCHEME).setCellValue("Scheme Applied");
        hrow.createCell(CATEGORY).setCellValue("Awarded Category");
        hrow.createCell(TENDER_DESC).setCellValue("Tender Objective/Details");
        hrow.createCell(TENDER_NO).setCellValue("Tender Number");
        hrow.createCell(CONTRACT_TYPE).setCellValue("Contract Doc Type");
        hrow.createCell(PROCUREMENT_METHOD).setCellValue("Procurement Method");
        hrow.createCell(CONTRACT_NO).setCellValue("Contract Number");
        hrow.createCell(CONTRACT_VALUE).setCellValue("Contract Value");
    }

    private void writeRow(XSSFRow row, TenderProcess tenderProcess) {
        Contract contract = tenderProcess.getSingleContract();
        Tender tender = tenderProcess.getSingleTender();
        Supplier awardee = contract.getAwardee();

        row.createCell(COMPANY_NAME).setCellValue(awardee.getLabel());
        row.createCell(DIRECTORS).setCellValue(prequalifiedSupplierService.find(awardee, tenderProcess)
                .map(this::extractDirectors)
                .orElse(null));
        row.createCell(SCHEME).setCellValue("AGPO");
        row.createCell(CATEGORY).setCellValue(
                Optional.of(contract.getTargetGroup()).map(TargetGroup::getLabel).orElse(null));
        row.createCell(TENDER_DESC).setCellValue(tender.getObjective());
        row.createCell(TENDER_NO).setCellValue(tender.getTenderNumber());
        row.createCell(CONTRACT_TYPE).setCellValue(contract.getContractDocs().stream()
                .map(ContractDocument::getContractDocumentType)
                .filter(Objects::nonNull)
                .distinct()
                .map(ContractDocumentType::getLabel)
                .sorted()
                .collect(Collectors.joining(", ")));
        row.createCell(PROCUREMENT_METHOD).setCellValue(tender.getProcurementMethod().getLabel());
        row.createCell(CONTRACT_NO).setCellValue(contract.getReferenceNumber());
        row.createCell(CONTRACT_VALUE).setCellValue(contract.getContractValue().doubleValue());
    }

    private String extractDirectors(PrequalifiedSupplier prequalifiedSupplier) {
        return prequalifiedSupplier.getItems().stream()
                .map(PrequalifiedSupplierItem::getNonNullContact)
                .map(AbstractContact::getDirectors)
                .distinct()
                .sorted()
                .collect(Collectors.joining(", "));
    }

    private Specification<TenderProcess> getSpecification(Date from, Date to) {
        return (r, q, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            SetJoin<TenderProcess, Contract> contractJoin = r.join(TenderProcess_.contract);
            predicates.add(cb.equal(contractJoin.get(Contract_.status), DBConstants.Status.APPROVED));
            predicates.add(contractJoin.join(Contract_.targetGroup).get(TargetGroup_.label)
                    .in(DBConstants.TargetGroup.AGPO_CATEGORIES));
            if (from != null) {
                predicates.add(cb.greaterThanOrEqualTo(contractJoin.get(Contract_.contractDate), from));
            }
            if (to != null) {
                predicates.add(cb.lessThanOrEqualTo(contractJoin.get(Contract_.contractDate), to));
            }

            predicates.add(cb.equal(r.join(TenderProcess_.tender).get(Tender_.status), DBConstants.Status.APPROVED));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
