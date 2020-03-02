package org.devgateway.ocds.persistence.mongo;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author mpostelnicu
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MakueniMilestone extends Milestone {

    private Boolean delayed;

    private Boolean authorizePayment;

    public Boolean getDelayed() {
        return delayed;
    }

    public void setDelayed(Boolean delayed) {
        this.delayed = delayed;
    }

    public Boolean getAuthorizePayment() {
        return authorizePayment;
    }

    public void setAuthorizePayment(Boolean authorizePayment) {
        this.authorizePayment = authorizePayment;
    }
}
