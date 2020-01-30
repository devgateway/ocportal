package org.devgateway.toolkit.persistence.service.filterstate.form;

import org.devgateway.toolkit.persistence.dao.categories.Supplier;
import org.devgateway.toolkit.persistence.dao.form.AdministratorReport;
import org.devgateway.toolkit.persistence.dao.form.AdministratorReport_;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class AdministratorReportFilterState extends AbstractTenderProcessMakueniFilterState<AdministratorReport> {

    protected Supplier contractor;

    @Override
    public Specification<AdministratorReport> getSpecification() {
        return (root, query, cb) -> {
            final List<Predicate> predicates = new ArrayList<>();

            if (contractor != null) {
                cb.equal(root.get(AdministratorReport_.contractor), contractor);
            }

            predicates.add(super.getSpecification().toPredicate(root, query, cb));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    public Supplier getContractor() {
        return contractor;
    }

    public void setContractor(Supplier contractor) {
        this.contractor = contractor;
    }
}
