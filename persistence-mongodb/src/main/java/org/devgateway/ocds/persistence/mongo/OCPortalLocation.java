package org.devgateway.ocds.persistence.mongo;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author mpostelnicu
 */
@Document(collection = "location")
public class OCPortalLocation extends DefaultLocation {

    private OCPortalLocationType type;

    public OCPortalLocationType getType() {
        return type;
    }

    public void setType(OCPortalLocationType type) {
        this.type = type;
    }
}
