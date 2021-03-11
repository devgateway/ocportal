package org.devgateway.ocds.persistence.mongo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import org.devgateway.ocds.persistence.mongo.excel.annotation.ExcelExport;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MakueniOrganization extends Organization {

    @JsonProperty("targetGroup")
    @JsonPropertyDescription("The name of the AGPO Category. Eg PWD, Women, Youth, etc.")
    @ExcelExport
    private List<String> targetGroups = new ArrayList<>();

    public List<String> getTargetGroups() {
        return targetGroups;
    }

    public void setTargetGroups(List<String> targetGroups) {
        this.targetGroups = targetGroups;
    }
}

