package org.devgateway.toolkit.persistence.service.prequalification;

import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationYearRange;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationYearRange_;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.devgateway.toolkit.persistence.repository.prequalification.PrequalificationYearRangeRepository;
import org.devgateway.toolkit.persistence.service.BaseJpaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.metamodel.SingularAttribute;

@Service
@Transactional
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
    public SingularAttribute<? super PrequalificationYearRange, String> getTextAttribute() {
        return PrequalificationYearRange_.name;
    }
}