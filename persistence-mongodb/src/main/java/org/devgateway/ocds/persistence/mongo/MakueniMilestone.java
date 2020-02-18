package org.devgateway.ocds.persistence.mongo;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author mpostelnicu
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MakueniMilestone extends Milestone {

    private Boolean delayed;

    public Boolean getDelayed() {
        return delayed;
    }

    public void setDelayed(Boolean delayed) {
        this.delayed = delayed;
    }
}
