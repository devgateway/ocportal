package org.devgateway.ocds.persistence.mongo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import org.devgateway.ocds.persistence.mongo.excel.annotation.ExcelExport;

import java.util.LinkedHashSet;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MakueniTender extends Tender {
    /**
     * target Group
     * <p>
     * TThe name of the target group. Eg PWD, Women, Youth, etc.
     */
    @JsonProperty("targetGroup")
    @JsonPropertyDescription("The name of the target group. Eg PWD, Women, Youth, etc.")
    @ExcelExport
    private String targetGroup;

    private Set<MakueniLocation> locations = new LinkedHashSet<>();

    public String getTargetGroup() {
        return targetGroup;
    }

    public void setTargetGroup(String targetGroup) {
        this.targetGroup = targetGroup;
    }

    public Set<MakueniLocation> getLocations() {
        return locations;
    }

    public void setLocations(Set<MakueniLocation> locations) {
        this.locations = locations;
    }
}
