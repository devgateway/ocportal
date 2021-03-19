package org.devgateway.toolkit.persistence.repository.prequalification;

import org.devgateway.toolkit.persistence.dao.categories.Supplier;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationYearRange;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalifiedSupplier;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author Octavian Ciubotaru
 */
public interface PrequalifiedSupplierRepository extends BaseJpaRepository<PrequalifiedSupplier, Long> {

    @Query("select count(ps) "
            + "from PrequalifiedSupplier ps "
            + "where ps.supplier = :supplier "
            + "and ps.yearRange = :yearRange "
            + "and ps.id <> :exceptId")
    long countPrequalifiedSuppliers(
            @Param("supplier") Supplier supplier,
            @Param("yearRange") PrequalificationYearRange yearRange,
            @Param("exceptId") Long exceptId);
}
