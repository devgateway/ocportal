/**
 *
 */
package org.devgateway.ocds.persistence.mongo.repository.main;

import org.devgateway.ocds.persistence.mongo.Organization;
import org.devgateway.ocds.persistence.mongo.Organization.OrganizationType;
import org.springframework.data.mongodb.repository.Query;

import java.util.Collection;
import java.util.List;

/**
 * @author mpostelnicu
 *
 */
public interface OrganizationRepository extends GenericOrganizationRepository<Organization> {

    @Query(value = "{$and: [ { $or: [ {'_id' : ?0 }, " + "{'name': ?0 } ] }  , { 'roles': ?1 } ]}")
    Organization findByIdOrNameAndTypes(String idName, OrganizationType type);

    @Query(value = "{ $or: [ {'_id' : ?0 }, " + "{'name': ?0} ] }")
    Organization findByIdOrName(String idName);

    @Query(value = "{'_id': { $in : ?0 }}")
    List<Organization> findByIdCollection(Collection<String> idCol);

    @Query(value = "{$and: [{'_id': ?0} , { 'roles': ?1 } ] }")
    Organization findByAllIdsAndType(String id, OrganizationType type);
}
