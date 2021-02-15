package org.devgateway.toolkit.persistence.service.prequalification;

import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationSchema;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationYearRange;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.devgateway.toolkit.persistence.repository.norepository.TextSearchableRepository;
import org.devgateway.toolkit.persistence.repository.prequalification.PrequalificationSchemaRepository;
import org.devgateway.toolkit.persistence.repository.prequalification.PrequalificationYearRangeRepository;
import org.devgateway.toolkit.persistence.service.BaseJpaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrequalificationYearRangeServiceImpl extends BaseJpaServiceImpl<PrequalificationYearRange>
        implements PrequalificationYearRangeService {

    @Autowired
    private PrequalificationYearRangeRepository prequalificationYearRangeRepository;

    @Override
    protected BaseJpaRepository<PrequalificationYearRange, Long> repository() {
        return prequalificationYearRangeRepository;
    }

    @Override
    public PrequalificationYearRange newInstance() {
        return new PrequalificationYearRange();
    }

    @Override
    public TextSearchableRepository<PrequalificationYearRange, Long> textRepository() {
        return prequalificationYearRangeRepository;
    }
}