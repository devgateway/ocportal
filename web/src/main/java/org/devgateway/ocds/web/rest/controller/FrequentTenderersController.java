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
import org.devgateway.ocds.persistence.mongo.Award;
import org.devgateway.ocds.persistence.mongo.constants.MongoConstants;
import org.devgateway.ocds.web.rest.controller.request.YearFilterPagingRequest;
import org.devgateway.toolkit.persistence.mongo.aggregate.CustomProjectionOperation;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

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
@CacheConfig(keyGenerator = "genericPagingRequestKeyGenerator", cacheNames = "genericPagingRequestJson")
@Cacheable
public class FrequentTenderersController extends GenericOCDSController {


    @ApiOperation(value = "Detect frequent pairs of tenderers that apply together to bids."
            + "We are only showing pairs if they applied to more than one bid together."
            + "We are sorting the results after the number of occurences, descending."
            + "You can use all the filters that are available along with pagination options.")
    @RequestMapping(value = "/api/frequentTenderers", method = {RequestMethod.POST, RequestMethod.GET},
            produces = "application/json")
    public List<Document> frequentTenderers(@ModelAttribute @Valid final YearFilterPagingRequest filter) {

        DBObject project = new BasicDBObject();
        //project.put(Fields.UNDERSCORE_ID, 0);
        project.put("tendererId1", ref(MongoConstants.FieldNames.TENDER_TENDERERS_ID));
        project.put("tendererId2", ref(MongoConstants.FieldNames.TENDER_TENDERERS_ID));

        DBObject projectCmp = new BasicDBObject();
        projectCmp.put("tendererId1", 1);
        projectCmp.put("tendererId2", 1);
        projectCmp.put("cmp", new BasicDBObject("$cmp", Arrays.asList("$tendererId1", "$tendererId2")));

        Aggregation agg = newAggregation(
                match(where("tender.tenderers.1").exists(true)
                        .andOperator(getYearDefaultFilterCriteria(filter, getTenderDateField()
                        ))),
                new CustomProjectionOperation(project),
                unwind("tendererId1"),
                unwind("tendererId2"),
                new CustomProjectionOperation(projectCmp),
                match(where("cmp").is(1)), // keep only one pair, the one ordered tendererId1<tendererId2, drop the rest
                group("tendererId1", "tendererId2").count().as("pairCount"),
                match(where("pairCount").gt(1))
        );

        return releaseAgg(agg);
    }

    @ApiOperation(value = "Counts the tenders/awards where the given supplier id is among the winners. "
            + "This assumes there is only  one active award, which always seems to be the case, per tender. ")
    @RequestMapping(value = "/api/activeAwardsCount",
            method = {RequestMethod.POST, RequestMethod.GET},
            produces = "application/json")
    public List<Document> activeAwardsCount(@ModelAttribute @Valid final YearFilterPagingRequest filter) {

        Aggregation agg = newAggregation(
                match(where(MongoConstants.FieldNames.AWARDS_STATUS).is(Award.Status.active.toString())
                        .andOperator(getYearDefaultFilterCriteria(
                                filter,
                                MongoConstants.FieldNames.TENDER_PERIOD_START_DATE
                        ))),
                unwind("awards"),
                match(where(MongoConstants.FieldNames.AWARDS_STATUS).is(Award.Status.active.toString())
                        .andOperator(getYearDefaultFilterCriteria(
                                filter.awardFiltering(),
                                MongoConstants.FieldNames.TENDER_PERIOD_START_DATE
                        ))),
                unwind("awards.suppliers"),
                group(MongoConstants.FieldNames.AWARDS_SUPPLIERS_ID).count().as("cnt"),
                project("cnt").and(Fields.UNDERSCORE_ID).as("supplierId")
                        .andExclude(Fields.UNDERSCORE_ID)
        );

        return releaseAgg(agg);
    }

}
