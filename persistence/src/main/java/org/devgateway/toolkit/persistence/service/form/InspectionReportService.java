package org.devgateway.toolkit.persistence.service.form;

import org.devgateway.toolkit.persistence.dao.form.InspectionReport;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess;

/**
 * @author mpostelnicu
 */
public interface InspectionReportService extends AbstractMakueniEntityService<InspectionReport> {
    InspectionReport findByTenderProcess(TenderProcess tenderProcess);
}
