package org.devgateway.toolkit.persistence.service.excel;

import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.categories.ProcurementMethod_;
import org.devgateway.toolkit.persistence.dao.form.AwardNotification;
import org.devgateway.toolkit.persistence.dao.form.AwardNotificationItem;
import org.devgateway.toolkit.persistence.dao.form.AwardNotificationItem_;
import org.devgateway.toolkit.persistence.dao.form.AwardNotification_;
import org.devgateway.toolkit.persistence.dao.form.Contract;
import org.devgateway.toolkit.persistence.dao.form.Contract_;
import org.devgateway.toolkit.persistence.dao.form.Tender;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess_;
import org.devgateway.toolkit.persistence.dao.form.TenderQuotationEvaluation;
import org.devgateway.toolkit.persistence.dao.form.TenderQuotationEvaluation_;
import org.devgateway.toolkit.persistence.dao.form.Tender_;
import org.devgateway.toolkit.persistence.service.form.TenderProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.SetJoin;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Octavian Ciubotaru
 */
@Service
public class DirectProcurementsAboveExporter {

    private static final int TENDER_NR = 0;
    private static final int DEPARTMENT = 1;
    private static final int TENDER_OBJECTIVE = 2;
    private static final int TENDER_OPEN_DATE = 3;
    private static final int EVALUATION_END_DATE = 4;
    private static final int TENDER_AWARD_DATE = 5;
    private static final int AWARD_NOTIFICATION_DATE = 6;
    private static final int COMPANY_NAME = 7;
    private static final int CONTRACT_VALUE = 8;
    private static final int CONTRACT_SIGNING_DATE = 9;
    private static final int REFERENCE_NR = 10;
    private static final int CONTRACT_DESCRIPTION = 11;
    private static final int CONTRACT_START_DATE = 12;
    private static final int CONTRACT_COMPLETION_DATE = 13;

    @Autowired
    private TenderProcessService tenderProcessService;

    public boolean hasData(Date from, Date to, Set<String> directProcurementMethods) {
        return tenderProcessService.count(getSpecification(from, to, directProcurementMethods)) > 0;
    }

    public XSSFWorkbook export(Date from, Date to, Set<String> directProcurementMethods) {
        List<TenderProcess> list = tenderProcessService.findAll(getSpecification(from, to, directProcurementMethods));

        XSSFWorkbook wb = new XSSFWorkbook();

        XSSFSheet sheet = createSheet(wb);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");

        int rowNum = 1;
        for (TenderProcess process : list) {
            XSSFRow row = sheet.createRow(rowNum++);

            writeRow(sdf, process, row);
        }

        return wb;
    }

    private void writeRow(SimpleDateFormat sdf, TenderProcess process, XSSFRow row) {
        Tender tender = process.getSingleTender();
        Contract contract = process.getSingleContract();
        TenderQuotationEvaluation tenderQuotationEvaluation = process.getSingleTenderQuotationEvaluation();
        AwardNotification award = process.getSingleAwardNotification();


        row.createCell(TENDER_NR).setCellValue(tender.getTenderNumber());
        row.createCell(DEPARTMENT).setCellValue(process.getDepartment().getLabel());
        row.createCell(TENDER_OBJECTIVE).setCellValue(tender.getObjective());
        row.createCell(TENDER_OPEN_DATE).setCellValue(tenderQuotationEvaluation.getOpeningDate());
        row.createCell(EVALUATION_END_DATE).setCellValue(tenderQuotationEvaluation.getClosingDate());

        if (award.getItems().size() == 1) {
            AwardNotificationItem item = award.getItems().get(0);

            row.createCell(TENDER_AWARD_DATE).setCellValue(item.getTenderAwardDate());
            row.createCell(AWARD_NOTIFICATION_DATE).setCellValue(item.getAwardDate());
        } else {
            String tenderAwardDate = award.getItems().stream()
                    .map(AwardNotificationItem::getTenderAwardDate)
                    .map(d -> d == null ? "" : sdf.format(d))
                    .collect(Collectors.joining(", "));
            row.createCell(TENDER_AWARD_DATE).setCellValue(tenderAwardDate);

            String awardDate = award.getItems().stream()
                    .map(AwardNotificationItem::getAwardDate)
                    .map(d -> d == null ? "" : sdf.format(d))
                    .collect(Collectors.joining(", "));
            row.createCell(AWARD_NOTIFICATION_DATE).setCellValue(awardDate);
        }

        row.createCell(COMPANY_NAME).setCellValue(contract.getAwardee().getLabel());
        row.createCell(CONTRACT_VALUE).setCellValue(contract.getContractValue().doubleValue());
        row.createCell(CONTRACT_SIGNING_DATE).setCellValue(contract.getContractDate());
        row.createCell(REFERENCE_NR).setCellValue(contract.getReferenceNumber());
        row.createCell(CONTRACT_DESCRIPTION).setCellValue(contract.getDescription());
        row.createCell(CONTRACT_START_DATE).setCellValue(contract.getContractApprovalDate());
        row.createCell(CONTRACT_COMPLETION_DATE).setCellValue(contract.getExpiryDate());
    }

    private XSSFSheet createSheet(XSSFWorkbook wb) {
        XSSFSheet sheet = wb.createSheet();

        XSSFCellStyle dateCellStyle = wb.createCellStyle();
        dateCellStyle.setDataFormat(BuiltinFormats.getBuiltinFormat("d-mmm-yy"));

        XSSFCellStyle numberCellStyle = wb.createCellStyle();
        numberCellStyle.setDataFormat(BuiltinFormats.getBuiltinFormat("0.00"));

        XSSFCellStyle headerCellStyle = wb.createCellStyle();
        headerCellStyle.setWrapText(true);

        sheet.createFreezePane(0, 1);

        sheet.setDefaultColumnStyle(TENDER_OPEN_DATE, dateCellStyle);
        sheet.setDefaultColumnStyle(EVALUATION_END_DATE, dateCellStyle);
        sheet.setDefaultColumnStyle(TENDER_AWARD_DATE, dateCellStyle);
        sheet.setDefaultColumnStyle(AWARD_NOTIFICATION_DATE, dateCellStyle);
        sheet.setDefaultColumnStyle(CONTRACT_VALUE, numberCellStyle);
        sheet.setDefaultColumnStyle(CONTRACT_SIGNING_DATE, dateCellStyle);
        sheet.setDefaultColumnStyle(CONTRACT_START_DATE, dateCellStyle);
        sheet.setDefaultColumnStyle(CONTRACT_COMPLETION_DATE, dateCellStyle);

        sheet.setDefaultColumnWidth(12);
        sheet.setColumnWidth(TENDER_OBJECTIVE, 18 * 256);
        sheet.setColumnWidth(TENDER_OPEN_DATE, 18 * 256);
        sheet.setColumnWidth(AWARD_NOTIFICATION_DATE, 18 * 256);
        sheet.setColumnWidth(CONTRACT_COMPLETION_DATE, 18 * 256);

        XSSFRow hRow = sheet.createRow(0);
        hRow.setRowStyle(headerCellStyle);
        hRow.setHeightInPoints(24);
        hRow.createCell(TENDER_NR).setCellValue("Tender Number");
        hRow.createCell(DEPARTMENT).setCellValue("Department");
        hRow.createCell(TENDER_OBJECTIVE).setCellValue("Tender Objective/Details");
        hRow.createCell(TENDER_OPEN_DATE).setCellValue("Tender Opening Date");
        hRow.createCell(EVALUATION_END_DATE).setCellValue("Evaluation End Date");
        hRow.createCell(TENDER_AWARD_DATE).setCellValue("Tender Award Date");
        hRow.createCell(AWARD_NOTIFICATION_DATE).setCellValue("Award Notification Date");
        hRow.createCell(COMPANY_NAME).setCellValue("Company Name");
        hRow.createCell(CONTRACT_VALUE).setCellValue("Contract Value");
        hRow.createCell(CONTRACT_SIGNING_DATE).setCellValue("Contract Signing Date");
        hRow.createCell(REFERENCE_NR).setCellValue("Reference Number");
        hRow.createCell(CONTRACT_DESCRIPTION).setCellValue("Contract Description");
        hRow.createCell(CONTRACT_START_DATE).setCellValue("Contract Start Date");
        hRow.createCell(CONTRACT_COMPLETION_DATE).setCellValue("Contract Completion Date");
        return sheet;
    }

    private Specification<TenderProcess> getSpecification(Date from, Date to, Set<String> directProcurementMethods) {
        return (r, q, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            SetJoin<TenderProcess, AwardNotification> awardJoin = r.join(TenderProcess_.awardNotification);
            predicates.add(cb.equal(awardJoin.get(AwardNotification_.status), DBConstants.Status.APPROVED));
            predicates.add(cb.ge(
                    awardJoin.join(AwardNotification_.items).get(AwardNotificationItem_.awardValue),
                    DBConstants.Reports.DIRECT_PROCUREMENT_THRESHOLD));

            SetJoin<TenderProcess, Tender> tenderJoin = r.join(TenderProcess_.tender);
            predicates.add(cb.equal(tenderJoin.get(Tender_.status), DBConstants.Status.APPROVED));
            predicates.add(tenderJoin.join(Tender_.procurementMethod).get(ProcurementMethod_.label)
                    .in(directProcurementMethods));

            SetJoin<TenderProcess, Contract> contractJoin = r.join(TenderProcess_.contract);
            predicates.add(cb.equal(contractJoin.get(Contract_.status), DBConstants.Status.APPROVED));
            if (from != null) {
                predicates.add(cb.greaterThanOrEqualTo(contractJoin.get(Contract_.contractDate), from));
            }
            if (to != null) {
                predicates.add(cb.lessThanOrEqualTo(contractJoin.get(Contract_.contractDate), to));
            }

            predicates.add(cb.equal(
                    r.join(TenderProcess_.tenderQuotationEvaluation).get(TenderQuotationEvaluation_.status),
                    DBConstants.Status.APPROVED));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
