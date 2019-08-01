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

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;
import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * @author mpostelnicu
 */
@RestController
@CacheConfig(keyGenerator = "genericPagingRequestKeyGenerator", cacheNames = "genericPagingRequestJson")
@Cacheable
public class NumberPercentFirstTimeWinners extends GenericOCDSController {

    @ApiOperation(value = "")
    @RequestMapping(value = "/api/numberPercentFirstTimeWinners",
            method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json")
    public List<Document> numberPercentFirstTimeWinners(@ModelAttribute
                                                        @Valid final YearFilterPagingRequest filter) {

        DBObject project = new BasicDBObject();
        addYearlyMonthlyProjection(filter, project, ref(getAwardDateField()));
        project.put(MongoConstants.FieldNames.AWARDS_VALUE_AMOUNT, 1);
        project.put(
                "firstTimeWinner",
                new BasicDBObject("$toInt", ref(MongoConstants.FieldNames.AWARDS_FIRST_TIME_WINNER))
        );

        Aggregation agg = newAggregation(
                match(where(MongoConstants.FieldNames.AWARDS_VALUE_AMOUNT).exists(true)
                        .and(MongoConstants.FieldNames.AWARDS_DATE).exists(true)
                        .andOperator(getYearDefaultFilterCriteria(filter, getAwardDateField()))),
                unwind("awards"),
                unwind("awards.suppliers"),
                new CustomProjectionOperation(project),
                getYearlyMonthlyGroupingOperation(filter).sum("firstTimeWinner")
                        .as("firstTimeWinner"),
                transformYearlyGrouping(filter).andInclude("firstTimeWinner"),
                getSortByYearMonth(filter)
        );
        return releaseAgg(agg);
    }
}