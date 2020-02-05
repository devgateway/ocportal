package org.devgateway.toolkit.persistence.service.filterstate.form;

import org.devgateway.toolkit.persistence.dao.categories.Supplier;
import org.devgateway.toolkit.persistence.dao.form.AbstractImplTenderProcessMakueniEntity;
import org.devgateway.toolkit.persistence.dao.form.AbstractImplTenderProcessMakueniEntity_;
import org.devgateway.toolkit.persistence.dao.form.Contract;
import org.devgateway.toolkit.persistence.dao.form.Contract_;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractImplTenderProcessFilterState<T extends AbstractImplTenderProcessMakueniEntity>
        extends AbstractTenderProcessMakueniFilterState<T> {

    protected Contract contract;

    protected Supplier awardee;

    @Override
    public Specification<T> getSpecification() {
        return (root, query, cb) -> {
            final List<Predicate> predicates = new ArrayList<>();

            if (contract != null) {
                predicates.add(
                        cb.equal(root.get(AbstractImplTenderProcessMakueniEntity_.contract), contract));
            }

            if (awardee != null) {
                predicates.add(
                        cb.equal(
                                root.join(AbstractImplTenderProcessMakueniEntity_.contract).get(Contract_.awardee),
                                awardee
                        ));
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

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public Supplier getAwardee() {
        return awardee;
    }

    public void setAwardee(Supplier awardee) {
        this.awardee = awardee;
    }
}
