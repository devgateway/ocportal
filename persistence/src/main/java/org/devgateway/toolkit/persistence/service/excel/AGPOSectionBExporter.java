package org.devgateway.toolkit.persistence.service.excel;

import static org.devgateway.toolkit.persistence.service.excel.ExporterUtil.createCell;

import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.categories.Category;
import org.devgateway.toolkit.persistence.dao.categories.TargetGroup_;
import org.devgateway.toolkit.persistence.dao.form.Contract;
import org.devgateway.toolkit.persistence.dao.form.ContractDocument;
import org.devgateway.toolkit.persistence.dao.form.Contract_;
import org.devgateway.toolkit.persistence.dao.form.PaymentVoucher;
import org.devgateway.toolkit.persistence.dao.form.Tender;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess_;
import org.devgateway.toolkit.persistence.dao.form.Tender_;
import org.devgateway.toolkit.persistence.dto.NamedDateRange;
import org.devgateway.toolkit.persistence.service.form.TenderProcessService;
import org.devgateway.toolkit.persistence.service.prequalification.PrequalifiedSupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.SetJoin;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Octavian Ciubotaru
 */
@Service
public class AGPOSectionBExporter {

    private static final int INDEX = 0;
    private static final int SUPPLIER = 1;
    private static final int AGPO_CERT_ID = 2;
    private static final int DIRECTORS = 3;
    private static final int CONTRACT_DOC_TYPE = 4;
    private static final int TENDER_INFO = 5;
    private static final int PROCUREMENT_METHOD = 6;
    private static final int REFERENCE_NUMBER = 7;
    private static final int CONTRACT_VALUE = 8;
    private static final int PAYMENT_STATUS = 9;

    @Autowired
    private TenderProcessService tenderProcessService;

    @Autowired
    private PrequalifiedSupplierService prequalifiedSupplierService;

    public boolean hasData(NamedDateRange range) {
        return tenderProcessService.count(getSpecification(range)) > 0;
    }

    public XSSFWorkbook export(NamedDateRange range) {
        XSSFWorkbook workbook = new XSSFWorkbook();

        XSSFCellStyle numberCellStyle = workbook.createCellStyle();
        numberCellStyle.setDataFormat(BuiltinFormats.getBuiltinFormat("0.00"));

        XSSFSheet sheet = workbook.createSheet();
        sheet.setDefaultColumnWidth(20);

        addHeaderRow(sheet);

        addBody(sheet, range, numberCellStyle);

        return workbook;
    }

    private void addHeaderRow(XSSFSheet sheet) {
        XSSFRow row = sheet.createRow(0);
        row.createCell(INDEX).setCellValue("S/No.");
        row.createCell(SUPPLIER).setCellValue("Supplier Name");
        row.createCell(AGPO_CERT_ID).setCellValue("AGPO Certification ID");
        row.createCell(DIRECTORS).setCellValue("Directors");
        row.createCell(CONTRACT_DOC_TYPE).setCellValue("Contract Document Type");
        row.createCell(TENDER_INFO).setCellValue("Tender Number & Tender Objective/Details");
        row.createCell(PROCUREMENT_METHOD).setCellValue("Procurement Method");
        row.createCell(REFERENCE_NUMBER).setCellValue("Reference Number");
        row.createCell(CONTRACT_VALUE).setCellValue("Contract Value");
        row.createCell(PAYMENT_STATUS).setCellValue("Payment Status");
    }

    private void addBody(XSSFSheet sheet, NamedDateRange range, XSSFCellStyle numberCellStyle) {
        List<TenderProcess> tenderProcesses = tenderProcessService.findAll(getSpecification(range));

        Map<String, List<TenderProcess>> grouped = tenderProcesses.stream().collect(Collectors.groupingBy(
                tp -> tp.getSingleContract().getTargetGroup().getLabel(), Collectors.toList()));


        XSSFCellStyle categoryCellStyle = sheet.getWorkbook().createCellStyle();
        categoryCellStyle.setAlignment(HorizontalAlignment.CENTER);

        Set<String> subTotalAddresses = new TreeSet<>();

        for (String agpoCat : DBConstants.TargetGroup.AGPO_CATEGORIES) {
            List<TenderProcess> tenderProcessesForGroup = grouped.get(agpoCat);
            if (tenderProcessesForGroup == null) {
                continue;
            }

            int agpoRowNum = sheet.getLastRowNum() + 1;
            addAGPOHeaderRow(sheet, categoryCellStyle, agpoCat, agpoRowNum);

            for (int i = 0; i < tenderProcessesForGroup.size(); i++) {
                TenderProcess tp = tenderProcessesForGroup.get(i);
                addTenderProcessRow(sheet, i, tp, numberCellStyle);
            }

            int totalRowBum = sheet.getLastRowNum() + 1;
            addAGPOSubTotalRow(sheet, agpoRowNum, totalRowBum, numberCellStyle);

            subTotalAddresses.add(String.format("I%d", totalRowBum + 1));
        }

        int grandTotalRowNum = sheet.getLastRowNum() + 1;
        XSSFRow grandTotalRow = sheet.createRow(grandTotalRowNum);
        grandTotalRow.createCell(INDEX).setCellValue("Total for the Half year");
        sheet.addMergedRegion(new CellRangeAddress(grandTotalRowNum, grandTotalRowNum, INDEX, REFERENCE_NUMBER));
        createCell(grandTotalRow, CONTRACT_VALUE, numberCellStyle).setCellFormula(
                subTotalAddresses.stream().collect(Collectors.joining(",", "SUM(", ")")));
    }

    private void addAGPOSubTotalRow(XSSFSheet sheet, int agpoRowNum, int totalRowBum, XSSFCellStyle numberCellStyle) {
        XSSFRow agpoTotalRow = sheet.createRow(totalRowBum);
        agpoTotalRow.createCell(INDEX).setCellValue("Sub Total");
        sheet.addMergedRegion(new CellRangeAddress(totalRowBum, totalRowBum, INDEX, REFERENCE_NUMBER));
        createCell(agpoTotalRow, CONTRACT_VALUE, numberCellStyle).setCellFormula(
                String.format("SUM(I%d:I%d)", agpoRowNum + 2, totalRowBum));
    }

    private void addAGPOHeaderRow(XSSFSheet sheet, XSSFCellStyle categoryCellStyle, String agpoCat, int rownum) {
        XSSFRow agpoHeaderRow = sheet.createRow(rownum);
        XSSFCell cell = agpoHeaderRow.createCell(INDEX);
        cell.setCellValue(agpoCat);
        cell.setCellStyle(categoryCellStyle);
        sheet.addMergedRegion(new CellRangeAddress(rownum, rownum, INDEX, PAYMENT_STATUS));
    }

    private void addTenderProcessRow(XSSFSheet sheet, int i, TenderProcess tp, XSSFCellStyle numberCellStyle) {
        Contract contract = tp.getSingleContract();
        Tender tender = tp.getSingleTender();

        XSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
        row.createCell(INDEX).setCellValue(i + 1);
        row.createCell(SUPPLIER).setCellValue(contract.getAwardee().getLabel());
        row.createCell(AGPO_CERT_ID).setCellValue(contract.getAwardee().getAgpoRegistrationId());
        row.createCell(DIRECTORS).setCellValue(prequalifiedSupplierService.find(contract.getAwardee(), tender)
                .map(ExporterUtil::extractDirectors)
                .orElse(null));
        row.createCell(CONTRACT_DOC_TYPE).setCellValue(contract.getContractDocs().stream()
                .map(ContractDocument::getContractDocumentType)
                .map(Category::getLabel)
                .sorted()
                .collect(Collectors.joining(", ")));
        row.createCell(TENDER_INFO).setCellValue(Stream.of(tender.getTenderNumber(), tender.getObjective())
                .filter(Objects::nonNull)
                .collect(Collectors.joining(", ")));
        row.createCell(PROCUREMENT_METHOD).setCellValue(tender.getProcurementMethod().getLabel());
        row.createCell(REFERENCE_NUMBER).setCellValue(contract.getReferenceNumber());
        createCell(row, CONTRACT_VALUE, numberCellStyle).setCellValue(contract.getContractValue().doubleValue());
        row.createCell(PAYMENT_STATUS).setCellValue(getPaymentStatus(tp));
    }

    private String getPaymentStatus(TenderProcess tenderProcess) {
        if (tenderProcess.getPaymentVouchers().stream().anyMatch(this::approvedAndLastPayment)) {
            return "Paid in Full";
        } else if (tenderProcess.getPaymentVouchers().stream().anyMatch(this::approved)) {
            return "Paid Partially";
        } else {
            return "None Paid";
        }
    }

    private boolean approvedAndLastPayment(PaymentVoucher v) {
        return approved(v) && v.getLastPayment();
    }

    private boolean approved(PaymentVoucher v) {
        return DBConstants.Status.APPROVED.equals(v.getStatus());
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
