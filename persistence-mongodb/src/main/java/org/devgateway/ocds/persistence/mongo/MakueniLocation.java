package org.devgateway.ocds.persistence.mongo;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author mpostelnicu
 */
@Document(collection = "location")
public class MakueniLocation extends DefaultLocation {

    private MakueniLocationType type;

    public MakueniLocationType getType() {
        return type;
    }

    public void setType(MakueniLocationType type) {
        this.type = type;
    }
}
