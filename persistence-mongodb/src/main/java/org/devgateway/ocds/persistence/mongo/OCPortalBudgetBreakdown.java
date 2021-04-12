package org.devgateway.ocds.persistence.mongo;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OCPortalBudgetBreakdown {
    private String id;
    private Period period;

    private Amount amount;

    private OrganizationReference sourceParty;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    public OrganizationReference getSourceParty() {
        return sourceParty;
    }

    public void setSourceParty(OrganizationReference sourceParty) {
        this.sourceParty = sourceParty;
    }
}
