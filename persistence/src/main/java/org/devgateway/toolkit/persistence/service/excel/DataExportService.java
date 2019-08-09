package org.devgateway.toolkit.persistence.service.excel;

import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.excel.service.ExcelGeneratorService;
import org.devgateway.toolkit.persistence.service.form.ProcurementPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author idobre
 * @since 2019-06-05
 */
@Service
public class DataExportService {
    @Autowired
    private ProcurementPlanService procurementPlanService;

    @Autowired
    private ExcelGeneratorService excelGeneratorService;

    @Transactional(readOnly = true)
    public byte[] generateProcurementPlanExcel(final Long procurementPlanId) throws IOException {
        final Optional<ProcurementPlan> procurementPlan = procurementPlanService.findById(procurementPlanId);

        if (procurementPlan.isPresent()) {
            return excelGeneratorService.getExcelDownload(
                    new ArrayList<>(Arrays.asList(procurementPlan.get())), true);
        } else {
            return null;
        }
    }
}
