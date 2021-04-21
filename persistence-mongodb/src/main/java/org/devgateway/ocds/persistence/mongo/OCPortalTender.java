package org.devgateway.ocds.persistence.mongo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import org.devgateway.ocds.persistence.mongo.excel.annotation.ExcelExport;

import java.util.LinkedHashSet;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OCPortalTender extends Tender {
    /**
     * AGPO Category
     * <p>
     * TThe name of the AGPO Category. Eg PWD, Women, Youth, etc.
     */
    @JsonProperty("targetGroup")
    @JsonPropertyDescription("The name of the AGPO Category. Eg PWD, Women, Youth, etc.")
    @ExcelExport
    private String targetGroup;

    private Set<OCPortalLocation> locations = new LinkedHashSet<>();

    public String getTargetGroup() {
        return targetGroup;
    }

    public void setTargetGroup(String targetGroup) {
        this.targetGroup = targetGroup;
    }

    public Set<OCPortalLocation> getLocations() {
        return locations;
    }

    public void setLocations(Set<OCPortalLocation> locations) {
        this.locations = locations;
    }
}
