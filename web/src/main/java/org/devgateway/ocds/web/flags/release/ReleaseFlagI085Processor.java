package org.devgateway.ocds.web.flags.release;

import org.devgateway.ocds.persistence.mongo.Detail;
import org.devgateway.ocds.persistence.mongo.FlaggedRelease;
import org.devgateway.ocds.persistence.mongo.flags.AbstractFlaggedReleaseFlagProcessor;
import org.devgateway.ocds.persistence.mongo.flags.Flag;
import org.devgateway.ocds.persistence.mongo.flags.FlagType;
import org.devgateway.ocds.persistence.mongo.flags.preconditions.FlaggedReleasePredicates;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author mpostelnicu
 * <p>
 * i085 Bids are an exact percentage apart
 */
@Component
public class ReleaseFlagI085Processor extends AbstractFlaggedReleaseFlagProcessor {

    private MathContext mc = new MathContext(4, RoundingMode.HALF_UP);

    public BigDecimal getPercent(BigDecimal left, BigDecimal right) {
        if (left.equals(right) || right.equals(BigDecimal.ZERO)) {
            return null;
        }
        BigDecimal divide = left.divide(right, mc);
        return divide;
    }

    @PostConstruct
    @Override
    protected void setPredicates() {
        preconditionsPredicates = Collections.synchronizedList(Arrays.asList(
                FlaggedReleasePredicates.ACTIVE_AWARD, FlaggedReleasePredicates.OPEN_PROCUREMENT_METHOD,
                FlaggedReleasePredicates.MULTIPLE_BIDS
        ));
    }

    @Override
    protected void setFlag(Flag flag, FlaggedRelease flaggable) {
        flaggable.getFlags().setI085(flag);
    }

    @Override
    protected Set<FlagType> flagTypes() {
        return new HashSet<>(Arrays.asList(FlagType.FRAUD, FlagType.COLLUSION));
    }

    @Override
    protected Boolean calculateFlag(FlaggedRelease flaggable, StringBuffer rationale) {
        List<BigDecimal> bids = new ArrayList<>();
        for (Detail bid : flaggable.getBids().getDetails()) {
            bids.add(bid.getValue().getAmount());
        }
        for (int x = 0; x < bids.size(); x++) {
            for (int y = 0; y < bids.size(); y++) {
                BigDecimal percent = getPercent(bids.get(x), bids.get(y));

                //percent has to be non null, with scale of 2 or less, and less than twice the other number
                if (percent != null && percent.scale() <= 2 && percent.compareTo(BigDecimal.valueOf(2)) < 0) {
                    rationale.append("Bids ")
                            .append(bids.get(x))
                            .append(" and ")
                            .append(bids.get(y))
                            .append(" are an exact % apart: ")
                            .append(percent);
                    return true;
                }
            }
        }
        return false;
    }

}