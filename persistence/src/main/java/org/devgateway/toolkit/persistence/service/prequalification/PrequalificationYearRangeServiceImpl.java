package org.devgateway.toolkit.persistence.service.prequalification;

import com.google.common.collect.ImmutableSet;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationSchema;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationYearRange;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationYearRange_;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.devgateway.toolkit.persistence.repository.prequalification.PrequalificationYearRangeRepository;
import org.devgateway.toolkit.persistence.service.BaseJpaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import javax.persistence.metamodel.SingularAttribute;
import java.util.Collection;

@Service
@Transactional
public class PrequalificationYearRangeServiceImpl extends BaseJpaServiceImpl<PrequalificationYearRange>
        implements PrequalificationYearRangeService {

    private static final Collection<String> RELATED_COLLECTION_CACHES = ImmutableSet.of(
            PrequalificationSchema.class.getName() + ".prequalificationYearRanges");

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

    @Override
    public PrequalificationYearRange findDefault() {
        return repository().findOne((Specification<PrequalificationYearRange>) (root, cq, cb) -> {
            Subquery<Integer> subquery = cq.subquery(Integer.class);
            Root<PrequalificationYearRange> subRoot = subquery.from(PrequalificationYearRange.class);
            subquery.select(cb.max(subRoot.get(PrequalificationYearRange_.endYear)));
            return cb.equal(root.get(PrequalificationYearRange_.endYear), subquery);
        }).orElse(null);
    }

    @Override
    public long countByName(PrequalificationYearRange e) {
        return prequalificationYearRangeRepository.countByName(e.getId() == null ? -1 : e.getId(), e.getName());
    }

    @Override
    public long countByOverlapping(PrequalificationYearRange range) {
        return prequalificationYearRangeRepository.countByOverlapping(
                range.getId() == null ? -1 : range.getId(), range.getStartYear(),
                range.getEndYear());
    }

    @Override
    public Collection<String> getRelatedCollectionCaches() {
        return RELATED_COLLECTION_CACHES;
    }

}
