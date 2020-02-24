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

import io.swagger.annotations.ApiOperation;
import org.bson.Document;
import org.devgateway.ocds.web.rest.controller.request.YearFilterPagingRequest;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators;
import org.springframework.data.mongodb.core.aggregation.SetOperators;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static org.devgateway.ocds.persistence.mongo.constants.MongoConstants.FieldNames.CONTRACTS;
import static org.devgateway.ocds.persistence.mongo.constants.MongoConstants.FieldNames.CONTRACTS_DELAYED;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;
import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * @author mpostelnicu
 */
@RestController
public class ContractStatsController extends GenericOCDSController {


    @ApiOperation(value = "Number of Contracts with PMC recommendation to not pay")
    @RequestMapping(value = "/api/pmcNotAuthContracts", method = {RequestMethod.POST,
            RequestMethod.GET}, produces = "application/json")
    public List<Document> pmcNotAuthContracts(
            @ModelAttribute @Valid final YearFilterPagingRequest filter) {

        Aggregation agg = newAggregation(
                match(where(atLeastOne(CONTRACTS)).exists(true).andOperator(getYearDefaultFilterCriteria(
                        filter, getContractDateField()))),
                project(CONTRACTS),
                unwind(ref(CONTRACTS)),
                project().and(getContractDateField()).as("date").and(
                        SetOperators.AnyElementTrue.arrayAsSet(CONTRACTS_DELAYED))
                        .as("contractDelayed"),
                projectYearlyMonthly(filter, "date")
                        .and(ConditionalOperators.when(where("contractDelayed")
                                .is(true)).then(1).otherwise(0)).as("countDelayed")
                        .and(ConditionalOperators.when(where("contractDelayed")
                                .is(false)).then(1).otherwise(0)).as("countOnTime"),
                groupYearlyMonthly(filter)
                        .sum("countDelayed").as("countDelayed")
                        .sum("countOnTime").as("countOnTime"),
                transformYearlyGrouping(filter).andInclude("countOnTime", "countDelayed")
                        .andExpression("countDelayed / (countOnTime + countDelayed) * 100").as("percentDelayed")
        );

        return releaseAgg(agg);
    }

    @ApiOperation(value = "Number of contracts not completed on expected end date")
    @RequestMapping(value = "/api/delayedContracts", method = {RequestMethod.POST,
            RequestMethod.GET}, produces = "application/json")
    public List<Document> delayedContracts(
            @ModelAttribute @Valid final YearFilterPagingRequest filter) {

        Aggregation agg = newAggregation(
                match(where(atLeastOne(CONTRACTS)).exists(true).andOperator(getYearDefaultFilterCriteria(
                        filter, getContractDateField()))),
                project(CONTRACTS),
                unwind(ref(CONTRACTS)),
                project().and(getContractDateField()).as("date").and(
                        SetOperators.AnyElementTrue.arrayAsSet(CONTRACTS_DELAYED))
                        .as("contractDelayed"),
                projectYearlyMonthly(filter, "date")
                        .and(ConditionalOperators.when(where("contractDelayed")
                                .is(true)).then(1).otherwise(0)).as("countDelayed")
                        .and(ConditionalOperators.when(where("contractDelayed")
                                .is(false)).then(1).otherwise(0)).as("countOnTime"),
                groupYearlyMonthly(filter)
                        .sum("countDelayed").as("countDelayed")
                        .sum("countOnTime").as("countOnTime"),
                transformYearlyGrouping(filter).andInclude("countOnTime", "countDelayed")
                        .andExpression("countDelayed / (countOnTime + countDelayed) * 100").as("percentDelayed")
        );

        return releaseAgg(agg);
    }

}
