package org.devgateway.toolkit.persistence.fm;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Octavian Ciubotaru
 */
@ConfigurationProperties("fm")
public class DgFmProperties {

    private boolean emitProjected = false;

    private boolean active = false;

    private boolean printFeaturesInUseOnExit = false;

    private boolean defaultsForMissingFeatures = true;

    private boolean allowReconfiguration = false;

    private List<String> resources = new ArrayList<>();

    public boolean isEmitProjected() {
        return emitProjected;
    }

    public void setEmitProjected(boolean emitProjected) {
        this.emitProjected = emitProjected;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isPrintFeaturesInUseOnExit() {
        return printFeaturesInUseOnExit;
    }

    public void setPrintFeaturesInUseOnExit(boolean printFeaturesInUseOnExit) {
        this.printFeaturesInUseOnExit = printFeaturesInUseOnExit;
    }

    public boolean isDefaultsForMissingFeatures() {
        return defaultsForMissingFeatures;
    }

    public void setDefaultsForMissingFeatures(boolean defaultsForMissingFeatures) {
        this.defaultsForMissingFeatures = defaultsForMissingFeatures;
    }

    public boolean isAllowReconfiguration() {
        return allowReconfiguration;
    }

    public void setAllowReconfiguration(boolean allowReconfiguration) {
        this.allowReconfiguration = allowReconfiguration;
    }

    public List<String> getResources() {
        return resources;
    }

    public void setResources(List<String> resources) {
        this.resources = resources;
    }
}
