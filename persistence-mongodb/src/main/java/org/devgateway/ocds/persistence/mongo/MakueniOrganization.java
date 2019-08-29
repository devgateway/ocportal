package org.devgateway.ocds.persistence.mongo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import org.devgateway.ocds.persistence.mongo.excel.annotation.ExcelExport;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MakueniOrganization extends Organization {

    @JsonProperty("targetGroup")
    @JsonPropertyDescription("The name of the target group. Eg PWD, Women, Youth, etc.")
    @ExcelExport
    private String targetGroup;

    public String getTargetGroup() {
        return targetGroup;
    }

    public void setTargetGroup(String targetGroup) {
        this.targetGroup = targetGroup;
    }
}

