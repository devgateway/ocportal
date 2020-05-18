package org.devgateway.ocds.persistence.mongo;

/**
 * @author mpostelnicu
 */
public class MakueniContract extends Contract {
    private OrganizationReference contractor;

    public OrganizationReference getContractor() {
        return contractor;
    }

    public void setContractor(OrganizationReference contractor) {
        this.contractor = contractor;
    }
}
