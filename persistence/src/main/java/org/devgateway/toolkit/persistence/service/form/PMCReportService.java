package org.devgateway.toolkit.persistence.service.form;

import org.devgateway.toolkit.persistence.dao.form.PMCReport;
import org.devgateway.toolkit.persistence.service.TextSearchableService;

/**
 * @author mpostelnicu
 */
public interface PMCReportService extends AbstractImplTenderProcessMakueniEntityService<PMCReport>,
        TextSearchableService<PMCReport> {
}
