package org.devgateway.toolkit.persistence.service.form;

import org.devgateway.toolkit.persistence.dao.form.MEReport;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess;

/**
 * @author mpostelnicu
 */
public interface MEReportService extends AbstractMakueniEntityService<MEReport> {
    MEReport findByTenderProcess(TenderProcess tenderProcess);
}
