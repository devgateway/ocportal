package org.devgateway.toolkit.persistence.service.category;

import org.devgateway.toolkit.persistence.dao.categories.InspectionReportOutcome;
import org.devgateway.toolkit.persistence.repository.category.InspectionReportOutcomeRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.devgateway.toolkit.persistence.repository.norepository.TextSearchableRepository;
import org.devgateway.toolkit.persistence.service.BaseJpaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Octavian Ciubotaru
 */
@Service
public class InspectionReportOutcomeServiceImpl extends BaseJpaServiceImpl<InspectionReportOutcome>
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

    @Override
    public TextSearchableRepository<InspectionReportOutcome, Long> textRepository() {
        return repository;
    }
}
