package org.devgateway.ocds.persistence.mongo;

/**
 * @author mpostelnicu
 */
public class MakueniContract extends Contract {
    private MakueniOrganization contractor;

    public MakueniOrganization getContractor() {
        return contractor;
    }

    public void setContractor(MakueniOrganization contractor) {
        this.contractor = contractor;
    }
}
