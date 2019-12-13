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
package org.devgateway.ocds.web.rest.controller;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import io.swagger.annotations.ApiOperation;
import org.bson.Document;
import org.devgateway.ocds.persistence.mongo.constants.MongoConstants;
import org.devgateway.ocds.web.rest.controller.request.YearFilterPagingRequest;
import org.devgateway.toolkit.persistence.mongo.aggregate.CustomProjectionOperation;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * @author mpostelnicu
 */
@RestController
@CacheConfig(keyGenerator = "genericPagingRequestKeyGenerator", cacheNames = "genericPagingRequestJson")
@Cacheable
public class ProjectStatisticsController extends GenericOCDSController {

    @ApiOperation(value = "Calculates number of projects per year")
    @RequestMapping(value = "/api/numberOfProjectsByYear", method = {RequestMethod.POST,
            RequestMethod.GET}, produces = "application/json")
    public List<Document> numberOfProjectsByYear(@ModelAttribute @Valid final YearFilterPagingRequest filter) {

        DBObject project = new BasicDBObject();
        project.put(MongoConstants.FieldNames.PLANNING_BUDGET_PROJECT_ID, 1);
        addYearlyMonthlyProjection(filter, project, ref(getTenderDateField()));

        Aggregation agg = newAggregation(
                match(where(MongoConstants.FieldNames.PLANNING_BUDGET_PROJECT_ID).exists(true)
                        .and(getTenderDateField()).exists(true)
                        .andOperator(getYearDefaultFilterCriteria(filter, getTenderDateField()
                        ))),
                new CustomProjectionOperation(project),
                group(getYearlyMonthlyGroupingFields(filter)).count().as("count"),
                getSortByYearMonth(filter)
        );

        return releaseAgg(agg);
    }


}