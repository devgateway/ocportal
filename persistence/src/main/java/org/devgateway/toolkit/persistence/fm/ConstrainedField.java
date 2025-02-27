package org.devgateway.toolkit.persistence.fm;

import jakarta.persistence.metamodel.Attribute;
import java.util.Objects;

/**
 * @author Octavian Ciubotaru
 */
public class ConstrainedField {

    /**
     * Class where this field was declared. For specifying constraints specific to a subclass,
     * see {@link ConstraintInfo}.
     */
    private final Class<?> declaringJavaType;

    /**
     * Name of the field.
     */
    private final String fieldName;

    /**
     * Is the field collection valued?
     */
    private final boolean collection;

    /**
     * Java type of the field.
     */
    private final Class<?> fieldType;

    public ConstrainedField(Attribute<?, ?> attr) {
        this.declaringJavaType = attr.getDeclaringType().getJavaType();
        this.fieldName = attr.getName();
        this.collection = attr.isCollection();
        this.fieldType = attr.getJavaType();
    }

    public Class<?> getDeclaringJavaType() {
        return declaringJavaType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public boolean isCollection() {
        return collection;
    }

    public Class<?> getFieldType() {
        return fieldType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConstrainedField)) {
            return false;
        }
        ConstrainedField that = (ConstrainedField) o;
        return declaringJavaType.equals(that.declaringJavaType)
                && fieldName.equals(that.fieldName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(declaringJavaType, fieldName);
    }

    @Override
    public String toString() {
        return declaringJavaType.getSimpleName() + "." + fieldName;
    }
}
