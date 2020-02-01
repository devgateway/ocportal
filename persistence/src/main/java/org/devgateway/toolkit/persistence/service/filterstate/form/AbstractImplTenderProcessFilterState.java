package org.devgateway.toolkit.persistence.service.filterstate.form;

import org.devgateway.toolkit.persistence.dao.form.AbstractImplTenderProcessMakueniEntity;
import org.devgateway.toolkit.persistence.dao.form.AdministratorReport_;
import org.devgateway.toolkit.persistence.dao.form.Contract;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractImplTenderProcessFilterState<T extends AbstractImplTenderProcessMakueniEntity>
        extends AbstractTenderProcessMakueniFilterState<T> {

    protected Contract contract;

    @Override
    public Specification<T> getSpecification() {
        return (root, query, cb) -> {
            final List<Predicate> predicates = new ArrayList<>();

            if (contract != null) {
                cb.equal(root.get(AdministratorReport_.contract), contract);
            }

            predicates.add(super.getSpecification().toPredicate(root, query, cb));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    public Contract getContractor() {
        return contract;
    }

    public void setContractor(Contract contractor) {
        this.contract = contractor;
    }
}
