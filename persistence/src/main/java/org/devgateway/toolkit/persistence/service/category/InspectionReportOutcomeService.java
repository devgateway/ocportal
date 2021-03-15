package org.devgateway.toolkit.persistence.service.category;

import org.devgateway.toolkit.persistence.dao.categories.InspectionReportOutcome;
import org.devgateway.toolkit.persistence.service.TextSearchableService;

public interface InspectionReportOutcomeService extends CategoryService<InspectionReportOutcome>,
        TextSearchableService<InspectionReportOutcome> {
}
