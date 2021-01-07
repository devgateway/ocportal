package org.devgateway.toolkit.persistence.fm.entity;

import java.io.Serializable;

/**
 * @author Octavian Ciubotaru
 */
public class FeatureConfig implements Comparable<FeatureConfig>, Serializable {

    private final String resourceLocation;

    private final String content;

    public FeatureConfig(String resourceLocation, String content) {
        this.resourceLocation = resourceLocation;
        this.content = content;
    }

    public String getResourceLocation() {
        return resourceLocation;
    }

    public String getContent() {
        return content;
    }

    @Override
    public int compareTo(FeatureConfig o) {
        return resourceLocation.compareTo(o.resourceLocation);
    }
}
