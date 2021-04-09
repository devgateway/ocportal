package org.devgateway.ocds.persistence.mongo;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author mpostelnicu
 */
public class OCPortalContract extends Contract {
    private OrganizationReference contractor;

    private String targetGroup;

    private Set<OCPortalLocation> locations = new LinkedHashSet<>();

    public OrganizationReference getContractor() {
        return contractor;
    }

    public void setContractor(OrganizationReference contractor) {
        this.contractor = contractor;
    }

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
