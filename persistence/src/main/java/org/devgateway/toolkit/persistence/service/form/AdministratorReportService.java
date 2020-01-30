package org.devgateway.toolkit.persistence.service.form;

import org.devgateway.toolkit.persistence.dao.form.AdministratorReport;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess;

/**
 * @author gmutuhu
 */
public interface AdministratorReportService extends AbstractMakueniEntityService<AdministratorReport> {
    AdministratorReport findByTenderProcess(TenderProcess tenderProcess);
}
