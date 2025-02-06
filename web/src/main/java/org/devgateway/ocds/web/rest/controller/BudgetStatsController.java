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
import org.devgateway.ocds.web.rest.controller.request.YearFilterPagingRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static org.devgateway.ocds.persistence.mongo.constants.MongoConstants.FieldNames.CONTRACTS;
import static org.devgateway.ocds.persistence.mongo.constants.MongoConstants.FieldNames.CONTRACTS_VALUE_AMOUNT;
import static org.devgateway.ocds.persistence.mongo.constants.MongoConstants.FieldNames.PLANNING_BUDGETB;
import static org.devgateway.ocds.persistence.mongo.constants.MongoConstants.FieldNames.PLANNING_BUDGETB_AMOUNT;
import static org.devgateway.ocds.persistence.mongo.constants.MongoConstants.FieldNames.PLANNING_BUDGETB_ID;
import static org.devgateway.ocds.persistence.mongo.constants.MongoConstants.FieldNames.PLANNING_FISCAL_YEAR;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.facet;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.replaceRoot;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;

/**
 * @author mpostelnicu
 */
@RestController
public class BudgetStatsController extends GenericOCDSController {

    @Operation(summary = "Expenditure to-date vs. budget")
    @RequestMapping(value = "/api/expenditureToDateVsBudget", method = {RequestMethod.POST,
            RequestMethod.GET}, produces = "application/json")
    public List<Document> expenditureToDateVsBudget(
            @ModelAttribute @Valid final YearFilterPagingRequest filter) {

        Aggregation agg = newAggregation(
                match(getYearDefaultFilterCriteria(filter, getTenderDateField())),
                facet(
                        unwind(CONTRACTS, true),
                        group(PLANNING_FISCAL_YEAR)
                                .sum(CONTRACTS_VALUE_AMOUNT).as("expensedAmount"),
                        project("expensedAmount")
                                .and("_id").as("fiscalYear")
                ).as("expensed").and(
                        unwind(PLANNING_BUDGETB),
                        group(PLANNING_BUDGETB_ID)
                                .first(PLANNING_BUDGETB_AMOUNT).as("amount")
                                .first(PLANNING_FISCAL_YEAR).as("fiscalYear"),
                        group("fiscalYear").sum("amount").as("committedAmount"),
                        project("committedAmount")
                                .and("_id").as("fiscalYear")
                ).as("committed"),
                project().and("expensed").unionArrays("committed").as("union"),
                unwind("union"),
                replaceRoot("union"),
                group("fiscalYear")
                        .first("fiscalYear").as("fiscalYear")
                        .sum("expensedAmount").as("expensedAmount")
                        .sum("committedAmount").as("committedAmount"),
                sort(Sort.Direction.ASC, "fiscalYear")
        );

        List<Document> documents = releaseAgg(agg);

        for (Document document : documents) {
            double expensed = ((Number) document.get("expensedAmount")).doubleValue();
            double committed = ((Number) document.get("committedAmount")).doubleValue();
            double diff = committed - expensed;
            document.append("unabsorbedAmount", Math.max(diff, 0));
            document.append("overspentAmount", Math.max(-diff, 0));
        }

        return documents;
    }
}
