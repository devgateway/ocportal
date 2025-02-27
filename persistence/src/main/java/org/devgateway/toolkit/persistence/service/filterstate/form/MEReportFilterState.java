package org.devgateway.toolkit.persistence.service.filterstate.form;

import org.devgateway.toolkit.persistence.dao.categories.MEStatus;
import org.devgateway.toolkit.persistence.dao.form.MEReport;
import org.devgateway.toolkit.persistence.dao.form.MEReport_;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class MEReportFilterState extends AbstractImplTenderProcessFilterState<MEReport> {

    private MEStatus meStatus;


    @Override
    public Specification<MEReport> getSpecification() {
        return (root, query, cb) -> {
            final List<Predicate> predicates = new ArrayList<>();

            if (meStatus != null) {
                predicates.add(cb.equal(root.get(MEReport_.meStatus), meStatus));
            }

            predicates.add(super.getSpecification().toPredicate(root, query, cb));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    public MEStatus getMeStatus() {
        return meStatus;
    }

    public void setMeStatus(MEStatus meStatus) {
        this.meStatus = meStatus;
    }
}
