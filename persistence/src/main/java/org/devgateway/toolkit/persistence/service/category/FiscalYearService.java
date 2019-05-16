package org.devgateway.toolkit.persistence.service.category;

import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.service.BaseJpaService;
import org.devgateway.toolkit.persistence.service.TextSearchableService;

import java.util.List;

public interface FiscalYearService extends BaseJpaService<FiscalYear>, TextSearchableService<FiscalYear> {
    FiscalYear getLastFiscalYear();

    List<FiscalYear> getYearsWithData();

    List<FiscalYear> getYearsWithData(Long departmentId);
}
