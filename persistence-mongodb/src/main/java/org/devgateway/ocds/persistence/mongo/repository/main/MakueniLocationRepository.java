package org.devgateway.ocds.persistence.mongo.repository.main;

import org.devgateway.ocds.persistence.mongo.MakueniLocation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MakueniLocationRepository extends MongoRepository<MakueniLocation, String> {

    MakueniLocation findByDescription(String description);

    @Override
    <S extends MakueniLocation> List<S> saveAll(Iterable<S> entites);

    @Override
    <S extends MakueniLocation> S save(S entity);

    @Override
    <S extends MakueniLocation> List<S> insert(Iterable<S> entities);

    @Override
    <S extends MakueniLocation> S insert(S entity);
}
