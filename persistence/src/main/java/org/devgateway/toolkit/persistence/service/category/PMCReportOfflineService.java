package org.devgateway.toolkit.persistence.service.category;

import org.devgateway.toolkit.persistence.dto.PMCReportOffline;

import java.util.List;

public interface PMCReportOfflineService {

    List<PMCReportOffline> getPMCReports(Long userId);

    List<PMCReportOffline> updatePMCReports(Long userId, List<PMCReportOffline> listPMCReports);
}
