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
import org.devgateway.ocds.persistence.mongo.constants.MongoConstants;
import org.devgateway.ocds.web.rest.controller.request.YearFilterPagingRequest;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.limit;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;
import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 *
 * @author mpostelnicu
 *
 */
@RestController
@CacheConfig(keyGenerator = "genericPagingRequestKeyGenerator", cacheNames = "genericPagingRequestJson")
@Cacheable
public class TendersByItemClassification extends GenericOCDSController {

    public static final class Keys {
        public static final String TOTAL_TENDERS = "totalTenders";
        public static final String ITEMS_CLASSIFICATION = "items.classification";
        public static final String DESCRIPTION = "description";
        public static final String TOTAL_TENDER_AMOUNT = "totalTenderAmount";
        public static final String TOTAL_AMOUNT = "totalAmount";
        public static final String TOTAL_ITEMS = "totalItems";
        public static final String TENDER_COUNT = "tenderCount";
    }

    @ApiOperation(value = "This should show the number of tenders per tender.items.classification."
            + "The tender date is taken from tender.tenderPeriod.endDate.")
    @RequestMapping(value = "/api/tendersByItemClassification", method = { RequestMethod.POST,
            RequestMethod.GET }, produces = "application/json")
    public List<Document> tendersByItemClassification(
            @ModelAttribute @Valid final YearFilterPagingRequest filter) {


        Aggregation agg = newAggregation(
                match(where(MongoConstants.FieldNames.TENDER_PERIOD_END_DATE).exists(true)
                        .andOperator(getYearDefaultFilterCriteria(filter,
                                MongoConstants.FieldNames.TENDER_PERIOD_END_DATE))),
                unwind(MongoConstants.FieldNames.TENDER_ITEMS),
                project().and(MongoConstants.FieldNames.TENDER_ITEMS_CLASSIFICATION)
                        .as("itemsClassification")
                        .andInclude(Fields.UNDERSCORE_ID),
                group(Fields.UNDERSCORE_ID, "itemsClassification"),
                group("itemsClassification").count().as(Keys.TENDER_COUNT),
                sort(Direction.DESC, Keys.TENDER_COUNT),
                limit(10)
        );

        return releaseAgg(agg);
    }

    @ApiOperation(value = "Y axis = Top 10 of the Total Cost per item in Tender doc. Both, Total Cost and the item "
            + "details are being taken from the tender doc.\n"
            + "X Axis = Item Details (on from IFMIS. The description of each item will be the same defined in the "
            + "procurement plan but selected in the tender doc)")
    @RequestMapping(value = "/api/totalAmountByItem", method = {RequestMethod.POST,
            RequestMethod.GET}, produces = "application/json")
    public List<Document> totalAmountByItem(
            @ModelAttribute @Valid final YearFilterPagingRequest filter) {

        Aggregation agg = newAggregation(
                match(where(MongoConstants.FieldNames.TENDER_PERIOD_START_DATE).exists(true)
                        .and(MongoConstants.FieldNames.TENDER_ITEMS_UNIT_VALUE_AMOUNT).exists(true)
                        .and(MongoConstants.FieldNames.TENDER_ITEMS_QUANTITY).exists(true)
                        .andOperator(getYearDefaultFilterCriteria(filter,
                                MongoConstants.FieldNames.TENDER_PERIOD_END_DATE))),
                unwind("tender.items"),
                project().and(MongoConstants.FieldNames.TENDER_ITEMS_CLASSIFICATION)
                        .as(MongoConstants.FieldNames.TENDER_ITEMS_CLASSIFICATION)
                        .and(MongoConstants.FieldNames.TENDER_ITEMS_QUANTITY)
                        .multiply(MongoConstants.FieldNames.TENDER_ITEMS_UNIT_VALUE_AMOUNT).as(Keys.TOTAL_AMOUNT),
                group(MongoConstants.FieldNames.TENDER_ITEMS_CLASSIFICATION).count().as(Keys.TOTAL_ITEMS)
                        .sum(Keys.TOTAL_AMOUNT).as(Keys.TOTAL_AMOUNT),
                sort(Direction.DESC, Keys.TOTAL_AMOUNT), limit(10));

        return releaseAgg(agg);
    }


}