package org.devgateway.toolkit.web.fm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.devgateway.toolkit.web.fm.DgFmSubject;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DgFeature extends UnchainedDgFeature {

    @JsonIgnore
    private Set<DgFeature> chainedMixins = ConcurrentHashMap.newKeySet();

    @JsonIgnore
    private Set<DgFeature> chainedHardDeps = ConcurrentHashMap.newKeySet();

    @JsonIgnore
    private Set<DgFeature> chainedSoftDeps = ConcurrentHashMap.newKeySet();

    private Set<String> attachedLog =  ConcurrentHashMap.newKeySet();

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

    public Set<DgFeature> getChainedHardDeps() {
        return chainedHardDeps;
    }

    public void setChainedHardDeps(Set<DgFeature> chainedHardDeps) {
        this.chainedHardDeps = chainedHardDeps;
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

}
