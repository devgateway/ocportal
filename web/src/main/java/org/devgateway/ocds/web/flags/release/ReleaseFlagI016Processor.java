package org.devgateway.ocds.web.flags.release;


import org.devgateway.ocds.persistence.mongo.Classification;
import org.devgateway.ocds.persistence.mongo.FlaggedRelease;
import org.devgateway.ocds.persistence.mongo.Item;
import org.devgateway.ocds.persistence.mongo.flags.AbstractFlaggedReleaseFlagProcessor;
import org.devgateway.ocds.persistence.mongo.flags.Flag;
import org.devgateway.ocds.persistence.mongo.flags.FlagType;
import org.devgateway.ocds.persistence.mongo.flags.preconditions.FlaggedReleasePredicates;
import org.devgateway.ocds.web.rest.controller.SelectiveAwardsByBuyerItemSupplier;
import org.devgateway.ocds.web.rest.controller.request.YearFilterPagingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author mpostelnicu
 * <p>
 * (i016) Purchase splitting to avoid procurement thresholds
 */
@Component
public class ReleaseFlagI016Processor extends AbstractFlaggedReleaseFlagProcessor {

    private Map<String, Long> countMap;

    @Autowired
    private SelectiveAwardsByBuyerItemSupplier selectiveAwardsByBuyerItemSupplier;

    @Override
    protected void setFlag(Flag flag, FlaggedRelease flaggable) {
        flaggable.getFlags().setI016(flag);
    }

    @Override
    protected Boolean calculateFlag(FlaggedRelease flaggable, StringBuffer rationale) {
        String supplierId = flaggable.getAwards().iterator().next().getSuppliers().iterator().next().getId();
        String buyerId = flaggable.getBuyer().getId();
        Set<String> classIds = flaggable.getTender().getItems().stream().map(Item::getClassification)
                .map(Classification::getId).collect(Collectors.toSet());
        for (String c : classIds) {
            Long counts = countMap.get(buildKey(buyerId, supplierId, c));
            if (counts != null) {
                rationale.append("Buyer ").append(buyerId).append(" and supplier ").append(supplierId)
                        .append(" have ").append(counts).append(" wins for item ").append(c);
                return true;
            }
        }
        return false;
    }

    private String buildKey(String buyerId, String supplierId, String itemsClass) {
        return buyerId + "-" + supplierId + "-" + itemsClass;
    }

    @Override
    public void reInitialize() {
        List<SelectiveAwardsByBuyerItemSupplier.SelectiveAwardsResponse> selectiveAwardsResponses =
                selectiveAwardsByBuyerItemSupplier
                        .selectiveAwardsByBuyerItemSupplier(new YearFilterPagingRequest());
        countMap = new ConcurrentHashMap<>();
        selectiveAwardsResponses.forEach(r ->
                countMap.put(buildKey(r.getBuyerId(), r.getSupplierId(), r.getItemsClassification()), r.getCount())
        );
    }

    @Override
    protected Set<FlagType> flagTypes() {
        return new HashSet<FlagType>(Arrays.asList(FlagType.COLLUSION));
    }

    @Override
    @PostConstruct
    protected void setPredicates() {
        preconditionsPredicates = Collections.synchronizedList(
                Arrays.asList(
                        FlaggedReleasePredicates.ACTIVE_AWARD,
                        FlaggedReleasePredicates.SELECTIVE_PROCUREMENT_METHOD,
                        FlaggedReleasePredicates.TENDER_ITEMS_CLASSIFICATION
                ));
    }
}