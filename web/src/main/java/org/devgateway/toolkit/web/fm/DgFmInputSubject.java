package org.devgateway.toolkit.web.fm;

import org.danekja.java.util.function.serializable.SerializableBooleanSupplier;

/**
 * Interface that should be attached to objects for FM behavior, that also provide input means. This is not bound
 * to wicket component
 */
public interface DgFmInputSubject extends DgFmSubject {

    default Boolean isFmMandatory() {
        return isFmAttached() ? getFmService().isFeatureMandatory(getFmName()) : FmConstants.DEFAULT_MANDATORY;
    }

    default boolean isFmMandatory(SerializableBooleanSupplier requiredSupplier) {
        return requiredSupplier.getAsBoolean() || isFmMandatory();
    }
}
