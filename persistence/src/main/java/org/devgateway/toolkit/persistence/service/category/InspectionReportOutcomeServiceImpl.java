package org.devgateway.toolkit.persistence.service.category;

import org.devgateway.toolkit.persistence.dao.categories.InspectionReportOutcome;
import org.devgateway.toolkit.persistence.repository.category.InspectionReportOutcomeRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Octavian Ciubotaru
 */
@Service
public class InspectionReportOutcomeServiceImpl extends CategoryServiceImpl<InspectionReportOutcome>
        implements InspectionReportOutcomeService {

    @Autowired
    private InspectionReportOutcomeRepository repository;

    @Override
    public InspectionReportOutcome newInstance() {
        return new InspectionReportOutcome();
    }

    @Override
    protected BaseJpaRepository<InspectionReportOutcome, Long> repository() {
        return repository;
    }
}
