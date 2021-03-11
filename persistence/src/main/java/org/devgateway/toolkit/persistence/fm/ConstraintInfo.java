package org.devgateway.toolkit.persistence.fm;

import java.util.HashSet;
import java.util.Set;

/**
 * Defines all constraints that can be placed on a field.
 *
 * @author Octavian Ciubotaru
 */
public class ConstraintInfo {

    /**
     * Collection of classed where the field is required.
     */
    private final Set<Class<?>> requiredForJavaTypes = new HashSet<>();

    /**
     * Collection of classed where the field is optional. Keeping this collection allows to determine conflicts
     * when the same field was requested be both required and optional.
     */
    private final Set<Class<?>> optionalForJavaTypes = new HashSet<>();

    /**
     * Should the validate cascade for this specific field?
     */
    private final boolean cascadeValidation;

    public ConstraintInfo(boolean cascadeValidation) {
        this.cascadeValidation = cascadeValidation;
    }

    public Set<Class<?>> getRequiredForJavaTypes() {
        return requiredForJavaTypes;
    }

    public Set<Class<?>> getOptionalForJavaTypes() {
        return optionalForJavaTypes;
    }

    public boolean isCascadeValidation() {
        return cascadeValidation;
    }
}
