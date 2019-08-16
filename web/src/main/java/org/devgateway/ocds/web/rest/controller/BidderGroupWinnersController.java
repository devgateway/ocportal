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

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

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
public class BidderGroupWinnersController extends GenericOCDSController {

    public static class BidderGroupWinners implements Serializable {
        private List<String> tenderers;
        private List<String> supplier;
        private Long cnt;

        public List<String> getTenderers() {
            return tenderers;
        }

        public void setTenderers(List<String> tenderers) {
            this.tenderers = tenderers;
        }

        public List<String> getSupplier() {
            return supplier;
        }

        public void setSupplier(List<String> supplier) {
            this.supplier = supplier;
        }

        public Long getCnt() {
            return cnt;
        }

        public void setCnt(Long cnt) {
            this.cnt = cnt;
        }
    }

    public List<BidderGroupWinners> bidderGroupWinners() {
        Aggregation agg = newAggregation(
                match(where("tender.tenderers.0").exists(true)
                        .andOperator(getSinceDate(2))),
                project("tender.tenderers._id", "awards"),
                unwind("tenderers._id"),
                unwind("awards"),
                unwind("awards.suppliers"),
                sort(Sort.Direction.ASC, "tenderers._id"),
                group("_id").push("tenderers._id").as("tenderers")
                        .addToSet("awards.suppliers._id").as("supplier"),
                group("tenderers", "supplier").count().as("cnt"),
                match(where("cnt").gte(2))
        );

        return releaseAgg(agg, BidderGroupWinners.class);
    }


}
