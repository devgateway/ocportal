package org.devgateway.toolkit.persistence.service.form;

import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.form.PMCReport;

import java.util.Collection;
import java.util.List;

/**
 * @author mpostelnicu
 */
public interface PMCReportService extends AbstractImplTenderProcessClientEntityService<PMCReport> {

    List<PMCReport> getPMCReportsCreatedBy(String username, Collection<Department> departments);

    PMCReport saveReportAndUpdateTenderProcess(PMCReport entity);

    void onApproved(PMCReport report);
}
