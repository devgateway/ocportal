/**
 * 
 */
package org.devgateway.ocds.persistence.mongo.repository.main;

import org.devgateway.ocds.persistence.mongo.Release;
import org.springframework.data.mongodb.repository.Query;

import java.util.stream.Stream;

/**
 * @author mpostelnicu
 *
 */
public interface ReleaseRepository extends GenericReleaseRepository<Release> {

    @Query(value = "{$and: [ {'tender.tenderPeriod.endDate':  {$exists:true} }, {'awards.suppliers.0' : "
            + "{$exists:true}}]}",
            sort = "{ 'tender.tenderPeriod.endDate' : 1 }")
    Stream<Release> findAllNonEmptyEndDatesAwardSuppliersOrderByEndDateDesc();
}
