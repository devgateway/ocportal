package org.devgateway.ocds.persistence.mongo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

public enum OCPortalLocationType {

    ward("ward"),
    subcounty("subcounty");

    private final String value;
    private static final Map<String, OCPortalLocationType> CONSTANTS = new HashMap<String, OCPortalLocationType>();

    static {
        for (OCPortalLocationType c : values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    OCPortalLocationType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    @JsonValue
    public String value() {
        return this.value;
    }

    @JsonCreator
    public static OCPortalLocationType fromValue(String value) {
        OCPortalLocationType constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}
