package org.devgateway.ocds.persistence.mongo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import org.devgateway.ocds.persistence.mongo.excel.annotation.ExcelExport;

import java.util.Set;
import java.util.TreeSet;

public class MakueniItem extends Item {
    /**
     * AGPO Category
     * <p>
     * The name of the AGPO Category. Eg PWD, Women, Youth, etc.
     */
    @JsonProperty("targetGroup")
    @JsonPropertyDescription("The name of the AGPO Category. Eg PWD, Women, Youth, etc.")
    @ExcelExport
    private Set<String> targetGroup = new TreeSet<>();

    /**
     * The monetary value of a single unit, allocated to the AGPO Category
     */
    @JsonProperty("targetGroupValue")
    @ExcelExport
    @JsonPropertyDescription("The monetary value of a single unit, allocated to the AGPO Category")
    private Amount targetGroupValue;

    public Set<String> getTargetGroup() {
        return targetGroup;
    }

    public void setTargetGroup(Set<String> targetGroup) {
        this.targetGroup = targetGroup;
    }

    public Amount getTargetGroupValue() {
        return targetGroupValue;
    }

    public void setTargetGroupValue(Amount targetGroupValue) {
        this.targetGroupValue = targetGroupValue;
    }
}
