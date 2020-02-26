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
import org.devgateway.ocds.persistence.mongo.Contract;
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
import static org.devgateway.ocds.persistence.mongo.constants.MongoConstants.FieldNames.CONTRACTS_ID;
import static org.devgateway.ocds.persistence.mongo.constants.MongoConstants.FieldNames.CONTRACTS_MILESTONES;
import static org.devgateway.ocds.persistence.mongo.constants.MongoConstants.FieldNames.CONTRACTS_MILESTONE_CODE;
import static org.devgateway.ocds.persistence.mongo.constants.MongoConstants.FieldNames.CONTRACTS_PAYMENT_AUTHORIZED;
import static org.devgateway.ocds.persistence.mongo.constants.MongoConstants.FieldNames.CONTRACTS_STATUS;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
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


    @ApiOperation(value = "Cancelled / Terminated Contracts")
    @RequestMapping(value = "/api/cancelledContracts", method = {RequestMethod.POST,
            RequestMethod.GET}, produces = "application/json")
    public List<Document> cancelledContracts(
            @ModelAttribute @Valid final YearFilterPagingRequest filter) {
        clearTenderStatus(filter);
        Aggregation agg = newAggregation(
                match(where(atLeastOne(CONTRACTS)).exists(true).
                        andOperator(getYearDefaultFilterCriteria(
                                filter, getContractDateField()))),
                project(CONTRACTS),
                unwind(ref(CONTRACTS)),
                project(CONTRACTS).and(getContractDateField()).as("date"),
                projectYearlyMonthly(filter, "date")
                        .and(ConditionalOperators.when(where(CONTRACTS_STATUS)
                                .is(Contract.Status.cancelled.toString())).then(1).otherwise(0)).as("cancelled"),
                groupYearlyMonthly(filter).sum("cancelled").as("countCancelled"),
                transformYearlyGrouping(filter).andInclude("countCancelled")
        );

        return releaseAgg(agg);
    }


    @ApiOperation(value = "Number of Contracts with PMC recommendation to not pay")
    @RequestMapping(value = "/api/pmcNotAuthContracts", method = {RequestMethod.POST,
            RequestMethod.GET}, produces = "application/json")
    public List<Document> pmcNotAuthContracts(
            @ModelAttribute @Valid final YearFilterPagingRequest filter) {
        Aggregation agg = newAggregation(
                match(where(atLeastOne(CONTRACTS)).exists(true).
                        and(atLeastOne(CONTRACTS_MILESTONES)).exists(true).
                        andOperator(getYearDefaultFilterCriteria(
                                filter, getContractDateField()))),
                project(CONTRACTS),
                unwind(ref(CONTRACTS)),
                unwind(ref(CONTRACTS_MILESTONES)),
                project(CONTRACTS).and(getContractDateField()).as("date"),
                project().and("date").as("date").and(CONTRACTS_MILESTONE_CODE)
                        .as(CONTRACTS_MILESTONE_CODE).and(CONTRACTS_ID).as(CONTRACTS_ID)
                        .and(ConditionalOperators.when(where(CONTRACTS_PAYMENT_AUTHORIZED)
                                .is(false)).then(1).otherwise(0))
                        .as("countReportsNotAuthorized"),
                match(where(CONTRACTS_MILESTONE_CODE).is("PMCReport")),
                group(CONTRACTS_ID)
                        .sum("countReportsNotAuthorized").as("countReportsNotAuthorized")
                        .first("date").as("date"),
                projectYearlyMonthly(filter, "date").and(
                        ConditionalOperators.when(where("countReportsNotAuthorized").gt(0)).then(1).otherwise(0))
                        .as("countNotAuthorized")
                        .and(ConditionalOperators.when(where("countReportsNotAuthorized").is(0)).then(1).otherwise(0))
                        .as("countAuthorized"),
                groupYearlyMonthly(filter)
                        .sum("countAuthorized").as("countAuthorized")
                        .sum("countNotAuthorized").as("countNotAuthorized"),
                transformYearlyGrouping(filter).andInclude("countAuthorized", "countNotAuthorized")
                        .andExpression("countNotAuthorized / (countAuthorized + countNotAuthorized) * 100")
                        .as("percentNotAuthorized")
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
