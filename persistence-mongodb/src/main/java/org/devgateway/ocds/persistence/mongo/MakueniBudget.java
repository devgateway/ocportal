package org.devgateway.ocds.persistence.mongo;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MakueniBudget extends Budget {

    private MakueniBudgetBreakdown budgetBreakdown;

    public MakueniBudgetBreakdown getBudgetBreakdown() {
        return budgetBreakdown;
    }

    public void setBudgetBreakdown(MakueniBudgetBreakdown budgetBreakdown) {
        this.budgetBreakdown = budgetBreakdown;
    }
}
