package org.devgateway.ocds.web.flags.release;


import org.devgateway.ocds.persistence.mongo.FlaggedRelease;
import org.devgateway.ocds.persistence.mongo.flags.Flag;
import org.devgateway.ocds.persistence.mongo.flags.FlagType;
import org.devgateway.ocds.persistence.mongo.flags.preconditions.FlaggedReleasePredicates;
import org.devgateway.ocds.web.rest.controller.SelectiveAwardsByBuyerItemSupplier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author mpostelnicu
 * <p>
 * (i045) Supplier wins disproportionate number of contracts of the same type
 */
@Component
public class ReleaseFlagI045Processor extends ReleaseFlagI016Processor {

    @Override
    protected void setFlag(Flag flag, FlaggedRelease flaggable) {
        flaggable.getFlags().setI045(flag);
    }

    @Override
    public void reInitialize() {
        List<SelectiveAwardsByBuyerItemSupplier.SelectiveAwardsResponse> selectiveAwardsResponses =
                selectiveAwardsByBuyerItemSupplier
                        .selectiveAwardsByBuyerItemSupplier(3, null, 2);
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
                        FlaggedReleasePredicates.TENDER_ITEMS_CLASSIFICATION
                ));
    }
}