package org.devgateway.ocds.persistence.mongo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.LinkedHashSet;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OCPortalBudget extends Budget {

    @JsonDeserialize(as = LinkedHashSet.class)
    @JsonProperty("budgetBreakdown")
    private Set<OCPortalBudgetBreakdown> budgetBreakdown = new LinkedHashSet<>();

    public Set<OCPortalBudgetBreakdown> getBudgetBreakdown() {
        return budgetBreakdown;
    }

    public void setBudgetBreakdown(Set<OCPortalBudgetBreakdown> budgetBreakdown) {
        this.budgetBreakdown = budgetBreakdown;
    }
}
