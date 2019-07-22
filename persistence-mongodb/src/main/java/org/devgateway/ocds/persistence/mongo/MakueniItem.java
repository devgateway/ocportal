package org.devgateway.ocds.persistence.mongo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import org.devgateway.ocds.persistence.mongo.excel.annotation.ExcelExport;

public class MakueniItem extends Item {
    /**
     * target Group
     * <p>
     * TThe name of the target group. Eg PWD, Women, Youth, etc.
     */
    @JsonProperty("targetGroup")
    @JsonPropertyDescription("The name of the target group. Eg PWD, Women, Youth, etc.")
    @ExcelExport
    private String targetGroup;

    /**
     * The monetary value of a single unit, allocated to the target group
     */
    @JsonProperty("targetGroupValue")
    @ExcelExport
    @JsonPropertyDescription("The monetary value of a single unit, allocated to the target group")
    private Amount targetGroupValue;

    public String getTargetGroup() {
        return targetGroup;
    }

    public void setTargetGroup(String targetGroup) {
        this.targetGroup = targetGroup;
    }

    public Amount getTargetGroupValue() {
        return targetGroupValue;
    }

    public void setTargetGroupValue(Amount targetGroupValue) {
        this.targetGroupValue = targetGroupValue;
    }
}
