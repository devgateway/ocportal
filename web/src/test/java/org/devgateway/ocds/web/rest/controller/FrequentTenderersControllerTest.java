package org.devgateway.ocds.web.rest.controller;

import org.bson.Document;
import org.devgateway.ocds.web.rest.controller.request.YearFilterPagingRequest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mpostelnicu
 * @since 01/13/2017
 *
 * @see {@link AbstractEndPointControllerTest}
 */
public class FrequentTenderersControllerTest extends AbstractEndPointControllerTest {
    @Autowired
    private FrequentTenderersController frequentTenderersController;

    @Test
    public void frequentTenderersTest() throws Exception {
        final List<Document> frequentTenderers = frequentTenderersController
                .frequentTenderers(new YearFilterPagingRequest());

        Assert.assertEquals(0, frequentTenderers.size());
    }

    @Test
    public void activeAwardsCountTest() throws Exception {
        YearFilterPagingRequest filter = new YearFilterPagingRequest();
        ArrayList<String> leftBidderIds = new ArrayList<>();
        leftBidderIds.add("GB-COH-1234567844");
        filter.setLeftBidderIds(leftBidderIds);

        ArrayList<String> rightBidderIds = new ArrayList<>();
        rightBidderIds.add("GB-COH-1234567845");
        filter.setRightBidderIds(rightBidderIds);

        final List<List<Integer>> counts = frequentTenderersController
                .getActiveAwardsCountInBatch(filter);

        Assert.assertEquals(2, counts.size());
    }

}
