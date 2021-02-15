package org.devgateway.toolkit.persistence.service.prequalification;

import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationYearRange;
import org.devgateway.toolkit.persistence.service.BaseJpaService;
import org.devgateway.toolkit.persistence.service.TextSearchableService;

import java.util.List;

public interface PrequalificationYearRangeService extends BaseJpaService<PrequalificationYearRange>,
        TextSearchableService<PrequalificationYearRange> {

}
