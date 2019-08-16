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

import org.devgateway.ocds.persistence.mongo.Tender;
import org.devgateway.ocds.persistence.mongo.constants.MongoConstants;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
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
public class SelectiveAwardsByBuyerItemSupplier extends GenericOCDSController {

    public static class SelectiveAwardsResponse implements Serializable {
        private String buyerId;
        private String supplierId;
        private String itemsClassification;
        private long count;

        public String getBuyerId() {
            return buyerId;
        }

        public void setBuyerId(String buyerId) {
            this.buyerId = buyerId;
        }

        public String getSupplierId() {
            return supplierId;
        }

        public void setSupplierId(String supplierId) {
            this.supplierId = supplierId;
        }

        public String getItemsClassification() {
            return itemsClassification;
        }

        public void setItemsClassification(String itemsClassification) {
            this.itemsClassification = itemsClassification;
        }

        public long getCount() {
            return count;
        }

        public void setCount(long count) {
            this.count = count;
        }
    }

    private Criteria getProcMethodCriteria(Tender.ProcurementMethod method) {
        if (method == null) {
            return new Criteria();
        }
        return where(MongoConstants.FieldNames.TENDER_PROC_METHOD).is(method.toString());
    }



    public List<SelectiveAwardsResponse> selectiveAwardsByBuyerItemSupplier(int tresh,
                                                                            Tender.ProcurementMethod method,
                                                                            Integer years) {
        Aggregation agg = newAggregation(
                match(
                        getProcMethodCriteria(method)
                        .and(MongoConstants.FieldNames.AWARDS_VALUE).exists(true)
                        .and(MongoConstants.FieldNames.BUYER_ID).exists(true)
                        .and(MongoConstants.FieldNames.TENDER_ITEMS_CLASSIFICATION).exists(true)
                                .andOperator(getSinceDate(years))),
                unwind(MongoConstants.FieldNames.TENDER_ITEMS),
                unwind("awards"),
                unwind("awards.suppliers"),
                project().and(MongoConstants.FieldNames.BUYER_ID).as("buyerId")
                        .and(MongoConstants.FieldNames.AWARDS_SUPPLIERS_ID).as("supplierId")
                        .and(MongoConstants.FieldNames.TENDER_ITEMS_CLASSIFICATION_ID).as("itemsClassification")
                        .and(Fields.UNDERSCORE_ID).as("id"),
                group("id", "buyerId", "supplierId", "itemsClassification"),
                group("buyerId", "supplierId", "itemsClassification").count().as("count"),
                match(where("count").gte(tresh))
        );
        return releaseAgg(agg, SelectiveAwardsResponse.class);
    }
}