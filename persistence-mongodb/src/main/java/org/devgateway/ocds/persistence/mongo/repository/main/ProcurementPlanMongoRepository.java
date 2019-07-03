package org.devgateway.ocds.persistence.mongo.repository.main;

import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author idobre
 * @since 2019-07-02
 */
public interface ProcurementPlanMongoRepository extends MongoRepository<ProcurementPlan, String> {

}
