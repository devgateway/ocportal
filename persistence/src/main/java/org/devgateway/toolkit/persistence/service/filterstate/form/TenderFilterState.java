package org.devgateway.toolkit.persistence.service.filterstate.form;

import org.devgateway.toolkit.persistence.dao.form.Tender;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author idobre
 * @since 2019-04-02
 */
public class TenderFilterState extends AbstractPurchaseReqMakueniFilterState<Tender> {

    private String tenderTitle;

    @Override
    public Specification<Tender> getSpecification() {
        return (root, query, cb) -> {
            final List<Predicate> predicates = new ArrayList<>();

            predicates.add(super.getSpecification().toPredicate(root, query, cb));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
}
