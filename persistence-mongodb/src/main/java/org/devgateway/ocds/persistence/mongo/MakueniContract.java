package org.devgateway.ocds.persistence.mongo;

import org.devgateway.toolkit.persistence.dao.categories.TargetGroup;

/**
 * @author mpostelnicu
 */
public class MakueniContract extends Contract {
    private OrganizationReference contractor;

    private String targetGroup;

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
}
