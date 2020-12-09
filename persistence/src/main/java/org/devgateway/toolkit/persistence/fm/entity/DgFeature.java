package org.devgateway.toolkit.persistence.fm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.devgateway.toolkit.persistence.fm.DgFmSubject;

import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author mpostelnicu
 */
public class DgFeature extends UnchainedDgFeature {

    @JsonIgnore
    private Set<DgFeature> chainedMixins = ConcurrentHashMap.newKeySet();

    @JsonIgnore
    private Set<DgFeature> chainedEnabledDeps = ConcurrentHashMap.newKeySet();

    @NotNull
    @JsonIgnore
    private Set<DgFeature> chainedVisibleDeps = ConcurrentHashMap.newKeySet();

    @NotNull
    @JsonIgnore
    private Set<DgFeature> chainedMandatoryDeps = ConcurrentHashMap.newKeySet();

    @JsonIgnore
    private Set<DgFeature> chainedSoftDeps = ConcurrentHashMap.newKeySet();

    @JsonIgnore
    private Set<String> attachedLog = ConcurrentHashMap.newKeySet();

    public Set<String> getAttachedLog() {
        return attachedLog;
    }

    public void setAttachedLog(Set<String> attachedLog) {
        this.attachedLog = attachedLog;
    }

    public Set<DgFeature> getChainedMixins() {
        return chainedMixins;
    }

    public void setChainedMixins(Set<DgFeature> chainedMixins) {
        this.chainedMixins = chainedMixins;
    }

    public Set<DgFeature> getChainedSoftDeps() {
        return chainedSoftDeps;
    }

    public void setChainedSoftDeps(Set<DgFeature> chainedSoftDeps) {
        this.chainedSoftDeps = chainedSoftDeps;
    }

    public void addAttachedLog(String log) {
        attachedLog.add(log);
    }

    public void addAttachedLog(DgFmSubject attachedTo) {
        addAttachedLog(String.format("Attached to class %s", attachedTo.getClass().getSimpleName()));
    }

    public Set<DgFeature> getChainedEnabledDeps() {
        return chainedEnabledDeps;
    }

    public void setChainedEnabledDeps(Set<DgFeature> chainedEnabledDeps) {
        this.chainedEnabledDeps = chainedEnabledDeps;
    }

    public Set<DgFeature> getChainedVisibleDeps() {
        return chainedVisibleDeps;
    }

    public void setChainedVisibleDeps(Set<DgFeature> chainedVisibleDeps) {
        this.chainedVisibleDeps = chainedVisibleDeps;
    }

    public Set<DgFeature> getChainedMandatoryDeps() {
        return chainedMandatoryDeps;
    }

    public void setChainedMandatoryDeps(Set<DgFeature> chainedMandatoryDeps) {
        this.chainedMandatoryDeps = chainedMandatoryDeps;
    }
}
