package org.devgateway.toolkit.persistence.service.form;

import org.devgateway.toolkit.persistence.dao.form.PMCReport;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess;

/**
 * @author gmutuhu
 */
public interface PMCReportService extends AbstractMakueniEntityService<PMCReport> {
    PMCReport findByTenderProcess(TenderProcess tenderProcess);
}
