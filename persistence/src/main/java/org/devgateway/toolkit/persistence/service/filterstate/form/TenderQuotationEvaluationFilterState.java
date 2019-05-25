package org.devgateway.toolkit.persistence.service.filterstate.form;


import org.devgateway.toolkit.persistence.dao.form.TenderQuotationEvaluation;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author gmutuhu
 * @since 2019-04-02
 */
public class TenderQuotationEvaluationFilterState
        extends AbstractPurchaseReqMakueniFilterState<TenderQuotationEvaluation> {
    @Override
    public Specification<TenderQuotationEvaluation> getSpecification() {
        return (root, query, cb) -> {
            final List<Predicate> predicates = new ArrayList<>();

            predicates.add(super.getSpecification().toPredicate(root, query, cb));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
}
