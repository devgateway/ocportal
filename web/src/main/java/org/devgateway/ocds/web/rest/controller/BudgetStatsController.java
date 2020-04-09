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
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static org.devgateway.ocds.persistence.mongo.constants.MongoConstants.FieldNames.CONTRACTS;
import static org.devgateway.ocds.persistence.mongo.constants.MongoConstants.FieldNames.CONTRACTS_IMPL_TRANSACTIONS;
import static org.devgateway.ocds.persistence.mongo.constants.MongoConstants.FieldNames.CONTRACTS_IMPL_TRANSACTIONS_AMOUNT;
import static org.devgateway.ocds.persistence.mongo.constants.MongoConstants.FieldNames.PLANNING_BUDGETB_ID;
import static org.devgateway.ocds.persistence.mongo.constants.MongoConstants.FieldNames.PLANNING_BUDGETB_MEASURES_COMMITTED;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.facet;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.replaceRoot;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;

/**
 * @author mpostelnicu
 */
@RestController
public class BudgetStatsController extends GenericOCDSController {

    @ApiOperation(value = "Expenditure to-date vs. budget")
    @RequestMapping(value = "/api/expenditureToDateVsBudget", method = {RequestMethod.POST,
            RequestMethod.GET}, produces = "application/json")
    public List<Document> expenditureToDateVsBudget(
            @ModelAttribute @Valid final YearFilterPagingRequest filter) {
        Aggregation agg = newAggregation(
                match(getYearDefaultFilterCriteria(
                        filter, getContractDateField())
                        .orOperator(Criteria.where(PLANNING_BUDGETB_MEASURES_COMMITTED).exists(true),
                                Criteria.where(CONTRACTS_IMPL_TRANSACTIONS_AMOUNT).exists(true))),

                facet(unwind(CONTRACTS, true),
                        unwind(CONTRACTS_IMPL_TRANSACTIONS, true),
                        group().sum(CONTRACTS_IMPL_TRANSACTIONS_AMOUNT).as("amount"),
                        project("amount").and("Expensed").asLiteral().as("type")
                ).as("expensed")
                        .and(
                                group(PLANNING_BUDGETB_ID).first(PLANNING_BUDGETB_MEASURES_COMMITTED).as("amount"),
                                group().sum("amount").as("amount"),
                                project("amount").and("Committed").asLiteral().as("type")
                        )
                        .as("committed"),
                project().and("expensed").unionArrays("committed").as("union"),
                unwind("union"),
                replaceRoot("union")
        );

        return releaseAgg(agg);
    }


}
