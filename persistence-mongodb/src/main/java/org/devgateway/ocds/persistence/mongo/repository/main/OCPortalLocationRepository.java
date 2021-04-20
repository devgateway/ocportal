package org.devgateway.ocds.persistence.mongo.repository.main;

import org.devgateway.ocds.persistence.mongo.OCPortalLocation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OCPortalLocationRepository extends MongoRepository<OCPortalLocation, String> {

    OCPortalLocation findByDescription(String description);

    @Override
    <S extends OCPortalLocation> List<S> saveAll(Iterable<S> entites);

    @Override
    <S extends OCPortalLocation> S save(S entity);

    @Override
    <S extends OCPortalLocation> List<S> insert(Iterable<S> entities);

    @Override
    <S extends OCPortalLocation> S insert(S entity);
}
