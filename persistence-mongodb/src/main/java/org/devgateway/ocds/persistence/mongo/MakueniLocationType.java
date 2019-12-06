package org.devgateway.ocds.persistence.mongo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

public enum MakueniLocationType {

    ward("ward"),
    subcounty("subcounty");

    private final String value;
    private static final Map<String, MakueniLocationType> CONSTANTS = new HashMap<String, MakueniLocationType>();

    static {
        for (MakueniLocationType c : values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    MakueniLocationType(String value) {
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
    public static MakueniLocationType fromValue(String value) {
        MakueniLocationType constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}
