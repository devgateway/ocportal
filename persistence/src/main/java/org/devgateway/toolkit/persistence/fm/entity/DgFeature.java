package org.devgateway.toolkit.persistence.fm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.logging.log4j.util.TriConsumer;
import org.devgateway.toolkit.persistence.fm.DgFmSubject;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

/**
 * @author mpostelnicu
 */
public class DgFeature extends UnchainedDgFeature {

    /**
     * Denotes if we remembered the original values specified for mandatory, enabled or visible. After projection
     * phase the original value is lost.
     */
    private boolean frozen;

    private boolean rawMandatory;

    private boolean rawEnabled;

    private boolean rawVisible;

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

    public void freeze() {
        if (frozen) {
            throw new IllegalStateException("Already frozen!");
        }
        frozen = true;
        rawMandatory = getMandatory();
        rawVisible = getVisible();
        rawEnabled = getEnabled();
    }

    public List<Line> explain() {
        Writer writer = new Writer();

        explainVisible(writer, "feature");

        explainEnabled(writer, "feature");

        explainMandatory(writer, "feature");

        return writer.lines;
    }

    private static class Writer {

        private List<Line> lines;

        private Writer target;

        private boolean grayed;

        public Writer() {
            lines = new ArrayList<>();
        }

        public Writer(boolean grayed, Writer target) {
            this.grayed = grayed;
            this.target = target;
        }

        public void add(String text) {
            add(grayed, text);
        }

        private void add(boolean grayed, String text) {
            if (target != null) {
                target.add(grayed, "  " + text);
            } else {
                lines.add(new Line(grayed, text));
            }
        }

        public Writer indented() {
            return new Writer(grayed, this);
        }

        public Writer indentedAndGray() {
            return new Writer(true, this);
        }
    }

    public static class Line {

        private final boolean gray;

        private final String text;

        public Line(boolean gray, String text) {
            this.gray = gray;
            this.text = text;
        }

        public boolean isGray() {
            return gray;
        }

        public String getText() {
            return text;
        }

        @Override
        public String toString() {
            return (gray ? "G " : "W ") + text;
        }
    }

    private void explainVisible(Writer writer, String role) {
        explainProperty(writer, role, "visible", this::getVisible, () -> rawVisible,
                true, DgFeature::explainVisible, this::getChainedVisibleDeps);
    }

    private void explainEnabled(Writer writer, String role) {
        explainProperty(writer, role, "enabled", this::getEnabled, () -> rawEnabled,
                true, DgFeature::explainEnabled, this::getChainedEnabledDeps);
    }

    private void explainMandatory(Writer writer, String role) {
        explainProperty(writer, role, "mandatory", this::getMandatory, () -> rawMandatory,
                false, DgFeature::explainMandatory, this::getChainedMandatoryDeps);
    }

    private void explainProperty(Writer writer, String role,
            String property,
            BooleanSupplier isEnabled,
            BooleanSupplier isExplicitlyEnabled,
            boolean conjunction,
            TriConsumer<DgFeature, Writer, String> subExplainer,
            Supplier<Set<DgFeature>> chainedDeps) {
        writer.add(role + " " + getName() + " is " + (isEnabled.getAsBoolean() ? "" : "not ") + property + " because");

        Writer sub = writer.indented();

        sub.add(property + " is explicitly turned " + (isExplicitlyEnabled.getAsBoolean() ? "on" : "off"));

        if ((conjunction && !isEnabled.getAsBoolean() && !isExplicitlyEnabled.getAsBoolean())
                || (!conjunction && isEnabled.getAsBoolean() && isExplicitlyEnabled.getAsBoolean())) {
            sub = writer.indentedAndGray();
        }

        for (String mixin : getMixins()) {
            DgFeature dgFeature = getChainedMixins().stream()
                    .filter(f -> f.getName().equals(mixin))
                    .findFirst()
                    .orElse(null);
            if (dgFeature != null) {
                sub.add(conjunction ? "and" : "or");
                subExplainer.accept(dgFeature, sub, "mixin");
            }
        }

        for (String dep : getVisibleDeps()) {
            DgFeature dgFeature = chainedDeps.get().stream()
                    .filter(f -> f.getName().equals(dep))
                    .findFirst()
                    .orElse(null);
            if (dgFeature != null) {
                sub.add(conjunction ? "and" : "or");
                subExplainer.accept(dgFeature, sub, "dependency");
            }
        }
    }
}
