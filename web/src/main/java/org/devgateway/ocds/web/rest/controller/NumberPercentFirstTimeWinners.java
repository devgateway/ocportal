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

import io.swagger.v3.oas.annotations.Operation;
import org.bson.Document;
import org.devgateway.ocds.persistence.mongo.constants.MongoConstants.FieldNames;
import org.devgateway.ocds.web.rest.controller.request.YearFilterPagingRequest;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.facet;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;
import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * @author mpostelnicu
 */
@RestController
@CacheConfig(keyGenerator = "genericPagingRequestKeyGenerator", cacheNames = "genericPagingRequestJson")
@Cacheable
public class NumberPercentFirstTimeWinners extends GenericOCDSController {

    @Operation(summary = "")
    @RequestMapping(value = "/api/numberPercentFirstTimeWinners",
            method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json")
    public List<Document> numberPercentFirstTimeWinners(@ModelAttribute
                                                        @Valid final YearFilterPagingRequest filter) {


        Aggregation agg = newAggregation(
                facet(match(where(FieldNames.TENDER_PERIOD_END_DATE).exists(true).and("awards.suppliers.0")
                                .exists(true)), unwind("awards"), unwind("awards.suppliers"),
                        project(FieldNames.TENDER_PERIOD_END_DATE, "awards._id",
                                FieldNames.AWARDS_SUPPLIERS_ID),

                        sort(Sort.Direction.ASC, "suppliers._id", "tenderPeriod.endDate"),
                        group("suppliers._id").first("_id").as("awardId")
                                .first("tenderPeriod.endDate").as("date"),
                        projectYearlyMonthly(filter, "date"),
                        groupYearlyMonthly(filter).count().as("firstTimeWinnerCount")
                )
                        .as("firstTimeWinners")
                        .and(
                                match(where(FieldNames.AWARDS_DATE).exists(true)
                                        .andOperator(getYearDefaultFilterCriteria(filter, getAwardDateField()))),
                                unwind("awards"),
                                unwind("awards.suppliers"),
                                projectYearlyMonthly(filter, FieldNames.AWARDS_DATE),
                                groupYearlyMonthly(filter).count().as("countAwards")
                        ).as("allAwards"),
                unwind("allAwards", true), unwind("firstTimeWinners", true),
                project("firstTimeWinners", "allAwards").and(ConditionalOperators
                        .when(where("allAwards._id").is(ref("firstTimeWinners._id"))).then(1).otherwise(0))
                        .as("equal").and(ConditionalOperators
                        .when(where("firstTimeWinners.firstTimeWinnerCount").is(null)).then(0)
                        .otherwiseValueOf("firstTimeWinners.firstTimeWinnerCount")).as("firstTimeWinnerCount"),
                match(new Criteria().orOperator(where("equal").is(1), where("firstTimeWinners").exists(false))),
                project().and("allAwards._id").as("_id")
                        .and("allAwards.countAwards").as("countAwards")
                        .and("firstTimeWinnerCount").as("firstTimeWinnerAwards"),
                transformYearlyGroupingCostEff(filter).andInclude("firstTimeWinnerAwards", "countAwards").and(
                        "firstTimeWinnerAwards")
                        .divide("countAwards").as("percentFirstTimeWinner"),
                getSortByYearMonth(filter));

        return releaseAgg(agg);
    }
}
