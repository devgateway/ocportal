package org.devgateway.toolkit.persistence.fm;

import java.util.List;
import java.util.Map;

/**
 * @author Octavian Ciubotaru
 */
public class Constraints {

    private List<Class<?>> roots;

    private Map<ConstrainedField, ConstraintInfo> fieldConstraints;

    public Constraints(List<Class<?>> roots,
            Map<ConstrainedField, ConstraintInfo> fieldConstraints) {
        this.roots = roots;
        this.fieldConstraints = fieldConstraints;
    }

    public List<Class<?>> getRoots() {
        return roots;
    }

    public Map<ConstrainedField, ConstraintInfo> getFieldConstraints() {
        return fieldConstraints;
    }
}
