package org.devgateway.ocds.web.flags.release;


import org.devgateway.ocds.persistence.mongo.FlaggedRelease;
import org.devgateway.ocds.persistence.mongo.Organization;
import org.devgateway.ocds.persistence.mongo.flags.AbstractFlaggedReleaseFlagProcessor;
import org.devgateway.ocds.persistence.mongo.flags.Flag;
import org.devgateway.ocds.persistence.mongo.flags.FlagType;
import org.devgateway.ocds.persistence.mongo.flags.preconditions.FlaggedReleasePredicates;
import org.devgateway.ocds.web.rest.controller.BidderGroupWinnersController;
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
 * (i083) The same companies always bid, the same companies always win, and the same companies always lose
 */
@Component
public class ReleaseFlagI083Processor extends AbstractFlaggedReleaseFlagProcessor {

    private Map<String, BidderGroupWinnersController.BidderGroupWinners> map;

    @Autowired
    protected BidderGroupWinnersController bidderGroupWinnersController;

    @Override
    protected void setFlag(Flag flag, FlaggedRelease flaggable) {
        flaggable.getFlags().setI083(flag);
    }

    @Override
    protected Boolean calculateFlag(FlaggedRelease flaggable, StringBuffer rationale) {
        List<String> tenderers = flaggable.getTender()
                .getTenderers()
                .stream()
                .map(Organization::getId)
                .sorted()
                .collect(Collectors.toList());
        String key = buildKey(
                tenderers, flaggable.getAwards().iterator().next().getSuppliers().iterator().next().getId());
        BidderGroupWinnersController.BidderGroupWinners bidderGroupWinners = map.get(key);
        if (bidderGroupWinners != null) {
            rationale.append("Combination of bidders ").append(bidderGroupWinners.getTenderers())
                    .append(" with winner ").append(bidderGroupWinners.getSupplier().get(0)).append(" of ")
                    .append(bidderGroupWinners.getCnt()).append(" procurements in past 2 years");
            return true;
        }
        return false;
    }

    protected String buildKey(List<String> tenderers, String supplierId) {
        StringBuffer sb = new StringBuffer();
        tenderers.stream().sorted().forEach(s -> sb.append(s).append("-"));
        sb.append("s").append(supplierId);
        return sb.toString();
    }

    @Override
    public void reInitialize() {
        List<BidderGroupWinnersController.BidderGroupWinners> bidderGroupWinners =
                bidderGroupWinnersController.bidderGroupWinners();

        map = new ConcurrentHashMap<>();

        bidderGroupWinners.forEach(w -> map.put(buildKey(w.getTenderers(), w.getSupplier().get(0)), w));
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
                        FlaggedReleasePredicates.AT_LEAST_ONE_BID,
                        FlaggedReleasePredicates.OPEN_PROCUREMENT_METHOD.or(
                                FlaggedReleasePredicates.SELECTIVE_PROCUREMENT_METHOD
                        ).or(FlaggedReleasePredicates.LIMITED_PROCUREMENT_METHOD)
                ));
    }
}