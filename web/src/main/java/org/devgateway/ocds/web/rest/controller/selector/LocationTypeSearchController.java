/*******************************************************************************
 * Copyright (c) 2015 Development Gateway, Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License (MIT)
 * which accompanies this distribution, and is available at
 * https://opensource.org/licenses/MIT
 *
 * Contributors:
 * Development Gateway - initial API and implementation
 *******************************************************************************/
package org.devgateway.ocds.web.rest.controller.selector;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import io.swagger.annotations.ApiOperation;
import org.bson.Document;
import org.devgateway.ocds.persistence.mongo.constants.MongoConstants;
import org.devgateway.ocds.web.rest.controller.GenericOCDSController;
import org.devgateway.toolkit.persistence.mongo.aggregate.CustomOperation;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;

/**
 * @author mpostelnicu
 */
@RestController
@Cacheable
@CacheConfig(cacheNames = "locationTypesJson")
public class LocationTypeSearchController extends GenericOCDSController {

    @ApiOperation(value = "Location types from location extension")
    @RequestMapping(value = "/api/ocds/locationTypes/all", method = {RequestMethod.POST,
            RequestMethod.GET}, produces = "application/json")
    public List<Document> locationTypes() {

        DBObject project = new BasicDBObject(MongoConstants.FieldNames.TENDER_LOCATIONS_TYPE, 1);

        Aggregation agg = newAggregation(
                new CustomOperation(new Document("$project", project)),
                unwind(ref(MongoConstants.FieldNames.TENDER_LOCATIONS)),
                group(ref(MongoConstants.FieldNames.TENDER_LOCATIONS_TYPE))
        );

        return releaseAgg(agg);
    }

}