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
import io.swagger.v3.oas.annotations.Operation;
import org.bson.Document;
import org.devgateway.ocds.persistence.mongo.Award;
import org.devgateway.ocds.persistence.mongo.constants.MongoConstants;
import org.devgateway.ocds.web.rest.controller.request.YearFilterPagingRequest;
import org.devgateway.toolkit.persistence.mongo.aggregate.CustomProjectionOperation;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.LiteralOperators;
import org.springframework.data.mongodb.core.aggregation.SetOperators;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

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
public class FrequentTenderersController extends GenericOCDSController {


    @Operation(summary = "Detect frequent pairs of tenderers that apply together to bids."
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
                        .andOperator(getYearDefaultFilterCriteria(filter, getTenderDateField()))),
                new CustomProjectionOperation(project),
                unwind("tendererId1"),
                unwind("tendererId2"),
                new CustomProjectionOperation(projectCmp),
                match(where("cmp").is(1)), // keep only one pair, the one ordered tendererId1<tendererId2, drop the rest
                group("tendererId1", "tendererId2").count().as("pairCount"),
                match(where("pairCount").gt(1)),
                sort(Sort.Direction.DESC, "pairCount")
        );

        return releaseAgg(agg);
    }

    @Operation(summary = "Counts awards awarded to a supplier that participated in a bidding process with a "
            + "group of tenderers")
    @RequestMapping(value = "/api/activeAwardsCount",
            method = {RequestMethod.POST, RequestMethod.GET},
            produces = "application/json")
    public List<List<Integer>> getActiveAwardsCountInBatch(
            @ModelAttribute @Valid final YearFilterPagingRequest filter) {
        Assert.notNull(filter.getLeftBidderIds(), "Left bidder ids must not be null");
        Assert.notNull(filter.getRightBidderIds(), "Right bidder ids must not be null");
        Assert.notNull(filter.getLeftBidderIds().size() == filter.getRightBidderIds().size(),
                "Left and right bidders must have the same size");

        ArrayList<String> leftBidderIds = filter.getLeftBidderIds();
        ArrayList<String> rightBidderIds = filter.getRightBidderIds();
        filter.setLeftBidderIds(null);
        filter.setRightBidderIds(null);

        List<List<Integer>> allCounts = new ArrayList<>();
        for (int i = 0; i < leftBidderIds.size(); i++) {
            String leftBidderId = leftBidderIds.get(i);
            String rightBidderId = rightBidderIds.get(i);

            TreeSet<String> bidderId = new TreeSet<>();
            bidderId.add(leftBidderId);
            bidderId.add(rightBidderId);
            filter.setBidderId(bidderId);

            TreeSet<String> supplierId = new TreeSet<>();
            supplierId.add(leftBidderId);
            filter.setSupplierId(supplierId);

            List<Integer> counts = new ArrayList<>();
            counts.add(activeAwardsCount(filter));

            supplierId.clear();
            supplierId.add(rightBidderId);
            counts.add(activeAwardsCount(filter));

            allCounts.add(counts);
        }
        return allCounts;
    }

    private Integer activeAwardsCount(YearFilterPagingRequest filter) {
        Aggregation agg = newAggregation(
                match(where(MongoConstants.FieldNames.AWARDS_STATUS).is(Award.Status.active.toString())
                        .andOperator(getYearDefaultFilterCriteria(filter, getTenderDateField()))),
                project(
                        MongoConstants.FieldNames.TENDER_TENDERERS_ID,
                        MongoConstants.FieldNames.AWARDS_STATUS, MongoConstants.FieldNames.AWARDS_SUPPLIERS_ID
                ).and("awards").as("awards")
                        .and(LiteralOperators.Literal.asLiteral(filter.getBidderId().toArray()))
                        .as("filterTenderers"), unwind("awards"),
                match(where(MongoConstants.FieldNames.AWARDS_STATUS).is(Award.Status.active.toString())),
                unwind("awards.suppliers"),
                project(MongoConstants.FieldNames.AWARDS_SUPPLIERS_ID).and(
                        SetOperators.arrayAsSet("filterTenderers").intersects("tenderers._id"))
                        .as("commonToBoth"),
                match(where("commonToBoth").size(filter.getBidderId().size())),
                group().count().as("cnt")
        );

        List<Document> docs = releaseAgg(agg);
        if (docs.isEmpty()) {
            return 0;
        } else {
            return docs.get(0).getInteger("cnt");
        }
    }

}
