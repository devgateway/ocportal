package org.devgateway.ocds.persistence.mongo;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MakueniBudgetBreakdown {
    private String id;
    private Period period;
    private Map<String, String> classifications = new HashMap<>();
    private Map<String, BigDecimal> measures = new HashMap<>();

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

    public Map<String, String> getClassifications() {
        return classifications;
    }

    public void setClassifications(Map<String, String> classifications) {
        this.classifications = classifications;
    }

    public Map<String, BigDecimal> getMeasures() {
        return measures;
    }

    public void setMeasures(Map<String, BigDecimal> measures) {
        this.measures = measures;
    }
}
