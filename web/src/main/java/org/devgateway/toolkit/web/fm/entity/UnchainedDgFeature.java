package org.devgateway.toolkit.web.fm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.devgateway.toolkit.web.fm.FmConstants;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class UnchainedDgFeature implements Serializable {

    @NotEmpty
    private String name;

    private Boolean mandatory = FmConstants.DEFAULT_MANDATORY;

    private Boolean enabled = FmConstants.DEFAULT_ENABLED;

    private Boolean visible = FmConstants.DEFAULT_VISIBLE;

    @JsonIgnore
    private String hash;

    private String resourceLocation;

    @NotNull
    private Set<String> mixins = ConcurrentHashMap.newKeySet();
    @NotNull
    private Set<String> hardDeps = ConcurrentHashMap.newKeySet();
    @NotNull
    private Set<String> softDeps = ConcurrentHashMap.newKeySet();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getMandatory() {
        return mandatory;
    }

    public void setMandatory(Boolean mandatory) {
        this.mandatory = mandatory;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getResourceLocation() {
        return resourceLocation;
    }

    public void setResourceLocation(String resourceLocation) {
        this.resourceLocation = resourceLocation;
    }

    public Set<String> getMixins() {
        return mixins;
    }

    public void setMixins(Set<String> mixins) {
        this.mixins = mixins;
    }

    public Set<String> getHardDeps() {
        return hardDeps;
    }

    public void setHardDeps(Set<String> hardDeps) {
        this.hardDeps = hardDeps;
    }

    public Set<String> getSoftDeps() {
        return softDeps;
    }

    public void setSoftDeps(Set<String> softDeps) {
        this.softDeps = softDeps;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    @Override
    public String toString() {
        return getName();
    }
}
