package org.devgateway.toolkit.persistence.service.prequalification;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.devgateway.toolkit.persistence.dao.categories.Category;
import org.devgateway.toolkit.persistence.dao.categories.Supplier;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalifiedSupplierItem;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Octavian Ciubotaru
 */
public class PrequalifiedSupplierExporter {

    private static class Indexes {
        private static final int ITEM = 0;
        private static final int SUPPLIER_NAME = 1;
        private static final int PHONE_NR = 2;
        private static final int MAILING_ADDRESS = 3;
        private static final int EMAIL = 4;
        private static final int DIRECTORS = 5;
        private static final int TARGET_GROUPS = 6;
        private static final int LOCATION = 7;
    }

    public XSSFWorkbook export(List<PrequalifiedSupplierItem> items) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();

        exportHeader(sheet.createRow(0));
        sheet.createFreezePane(0, 1);

        sheet.setColumnWidth(Indexes.ITEM, numCharsToWidth(70));
        sheet.setColumnWidth(Indexes.SUPPLIER_NAME, numCharsToWidth(40));
        sheet.setColumnWidth(Indexes.PHONE_NR, numCharsToWidth(15));
        sheet.setColumnWidth(Indexes.MAILING_ADDRESS, numCharsToWidth(25));
        sheet.setColumnWidth(Indexes.EMAIL, numCharsToWidth(20));
        sheet.setColumnWidth(Indexes.DIRECTORS, numCharsToWidth(30));
        sheet.setColumnWidth(Indexes.TARGET_GROUPS, numCharsToWidth(20));
        sheet.setColumnWidth(Indexes.LOCATION, numCharsToWidth(30));

        for (int i = 0, itemsSize = items.size(); i < itemsSize; i++) {
            PrequalifiedSupplierItem item = items.get(i);
            exportRow(sheet.createRow(i + 1), item);
        }

        sheet.setAutoFilter(new CellRangeAddress(0, items.size(), Indexes.ITEM, Indexes.LOCATION));

        return workbook;
    }

    private int numCharsToWidth(int numChars) {
        return numChars * 256;
    }

    private void exportHeader(XSSFRow row) {
        row.createCell(Indexes.ITEM).setCellValue("Item");
        row.createCell(Indexes.SUPPLIER_NAME).setCellValue("Supplier Name");
        row.createCell(Indexes.PHONE_NR).setCellValue("Phone Number");
        row.createCell(Indexes.MAILING_ADDRESS).setCellValue("Mailing Address");
        row.createCell(Indexes.EMAIL).setCellValue("Email");
        row.createCell(Indexes.DIRECTORS).setCellValue("Directors");
        row.createCell(Indexes.TARGET_GROUPS).setCellValue("Target Groups");
        row.createCell(Indexes.LOCATION).setCellValue("Location");
    }

    private void exportRow(XSSFRow row, PrequalifiedSupplierItem item) {
        Supplier supplier = item.getParent().getSupplier();

        XSSFCell itemCell = row.createCell(Indexes.ITEM);
        itemCell.setCellValue(item.getItem().toString(item.getParent().getYearRange()));

        XSSFCell supplierCell = row.createCell(Indexes.SUPPLIER_NAME);
        supplierCell.setCellValue(supplier.getLabel());

        XSSFCell phoneCell = row.createCell(Indexes.PHONE_NR);
        phoneCell.setCellValue(item.getNonNullContact().getPhoneNumber());

        XSSFCell mailingAddressCell = row.createCell(Indexes.MAILING_ADDRESS);
        mailingAddressCell.setCellValue(item.getNonNullContact().getMailingAddress());

        XSSFCell emailCell = row.createCell(Indexes.EMAIL);
        emailCell.setCellValue(item.getNonNullContact().getEmail());

        XSSFCell directorsCell = row.createCell(Indexes.DIRECTORS);
        directorsCell.setCellValue(item.getNonNullContact().getDirectors());

        XSSFCell agpoCategoriesCell = row.createCell(Indexes.TARGET_GROUPS);
        agpoCategoriesCell.setCellValue(supplier.getTargetGroup().getLabel());

        XSSFCell locationCell = row.createCell(Indexes.LOCATION);
        locationCell.setCellValue(
                Stream.<Category>concat(supplier.getSubcounties().stream(), supplier.getWards().stream())
                        .map(Category::getLabel)
                        .collect(Collectors.joining(", ")));
    }
}
