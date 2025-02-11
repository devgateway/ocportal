package org.devgateway.toolkit.persistence.service.prequalification;

import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationYearRange;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationYearRange_;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.devgateway.toolkit.persistence.repository.prequalification.PrequalificationYearRangeRepository;
import org.devgateway.toolkit.persistence.service.BaseJpaServiceImpl;
import org.devgateway.toolkit.persistence.util.FiscalYearUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import jakarta.persistence.metamodel.SingularAttribute;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;

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
    public PrequalificationYearRange findByDate(Date date) {
        LocalDate localDate = LocalDate.from(date.toInstant().atZone(ZoneId.systemDefault()));
        int fy = FiscalYearUtil.getFiscalYear(localDate);
        return repository()
                .findOne((Specification<PrequalificationYearRange>) (root, cq, cb) -> cb.and(
                        cb.le(root.get(PrequalificationYearRange_.startYear), fy),
                        cb.ge(root.get(PrequalificationYearRange_.endYear), fy)))
                .orElse(null);
    }
}
