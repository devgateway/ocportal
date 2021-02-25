package org.devgateway.toolkit.persistence.service.prequalification;

import org.devgateway.toolkit.persistence.dao.categories.Supplier;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationYearRange;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalifiedSupplier;
import org.devgateway.toolkit.persistence.service.BaseJpaService;

/**
 * @author Octavian Ciubotaru
 */
public interface PrequalifiedSupplierService extends BaseJpaService<PrequalifiedSupplier> {

    boolean isSupplierPrequalified(Supplier supplier, PrequalificationYearRange yearRange, Long exceptId);

    PrequalifiedSupplier find(Supplier supplier, PrequalificationYearRange yearRange);
}
