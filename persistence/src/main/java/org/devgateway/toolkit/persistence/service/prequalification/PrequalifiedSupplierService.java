package org.devgateway.toolkit.persistence.service.prequalification;

import org.devgateway.toolkit.persistence.dao.categories.Supplier;
import org.devgateway.toolkit.persistence.dao.form.Bid;
import org.devgateway.toolkit.persistence.dao.form.Tender;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationYearRange;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalifiedSupplier;
import org.devgateway.toolkit.persistence.service.BaseJpaService;

import java.util.List;
import java.util.Optional;

/**
 * @author Octavian Ciubotaru
 */
public interface PrequalifiedSupplierService extends BaseJpaService<PrequalifiedSupplier> {

    boolean isSupplierPrequalified(Supplier supplier, PrequalificationYearRange yearRange, Long exceptId);

    Optional<PrequalifiedSupplier> find(Supplier supplier, Tender tender);

    default List<String> findItemsForBid(Bid bid) {
        return findItemsForBid(bid, bid.getParent().getTenderProcess().getSingleTender());
    }

    List<String> findItemsForBid(Bid bid, Tender tender);

    Optional<PrequalifiedSupplier> find(Supplier supplier, PrequalificationYearRange yearRange);
}
