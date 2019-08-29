package org.devgateway.ocds.web.flags.release;


import org.devgateway.ocds.persistence.mongo.FlaggedRelease;
import org.devgateway.ocds.persistence.mongo.Tender;
import org.devgateway.ocds.persistence.mongo.flags.Flag;
import org.devgateway.ocds.persistence.mongo.flags.preconditions.FlaggedReleasePredicates;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collections;

/**
 * @author mpostelnicu
 *         <p>
 *         i180 Contractor receives multiple single-source/non-competitive contracts from a single procuring entity
 *         during a defined time period
 */
@Component
public class ReleaseFlagI180Processor extends ReleaseFlagI077Processor {

    private static final Integer MAX_AWARDS = 2;

    @Override
    public Integer getMaxAwards() {
        return MAX_AWARDS;
    }

    @Override
    protected void setFlag(Flag flag, FlaggedRelease flaggable) {
        flaggable.getFlags().setI180(flag);
    }

    @Override
    protected String getProcurementMethod() {
        return Tender.ProcurementMethod.direct.toString();
    }

    @PostConstruct
    @Override
    protected void setPredicates() {
        preconditionsPredicates = Collections.synchronizedList(
                Arrays.asList(FlaggedReleasePredicates.ACTIVE_AWARD_WITH_DATE,
                        FlaggedReleasePredicates.BUYER,
                        FlaggedReleasePredicates.DIRECT_PROCUREMENT_METHOD
                ));

        reInitialize();
    }

}