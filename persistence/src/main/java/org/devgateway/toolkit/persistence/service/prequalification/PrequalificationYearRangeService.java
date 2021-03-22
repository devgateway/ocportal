package org.devgateway.toolkit.persistence.service.prequalification;

import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationYearRange;
import org.devgateway.toolkit.persistence.service.BaseJpaService;
import org.devgateway.toolkit.persistence.service.TextSearchableService;

import java.util.Date;

public interface PrequalificationYearRangeService extends BaseJpaService<PrequalificationYearRange>,
        TextSearchableService<PrequalificationYearRange> {

    PrequalificationYearRange findDefault();

    long countByName(PrequalificationYearRange range);

    long countByOverlapping(PrequalificationYearRange range);

    PrequalificationYearRange findByDate(Date date);
}
