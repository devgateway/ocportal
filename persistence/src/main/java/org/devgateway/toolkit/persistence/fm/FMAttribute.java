package org.devgateway.toolkit.persistence.fm;

import org.devgateway.toolkit.persistence.jpa.JPAAttribute;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * TODO this could reference a list of constraints, now it is used to point to non-null/not-empty constraints.
 *
 * @author Octavian Ciubotaru
 */
public class FMAttribute {

    /**
     * FM name that controls the behavior of this field.
     */
    private final String fmName;

    /**
     * Type representing the field.
     */
    private final Class<?> javaType;

    /**
     * Type where the field is declared.
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

    public FMAttribute(Class<?> javaType, JPAAttribute attr,
            String fmName) {
        this.javaType = javaType;
        this.declaringJavaType = attr.getDeclaringType();
        this.fieldName = attr.getName();
        this.collection = attr.isCollection();
        this.fmName = fmName;

        Assert.isTrue(declaringJavaType.isAssignableFrom(javaType),
                () -> String.format("%s does not a super for %s", declaringJavaType, javaType));
    }

    public String getFMName() {
        return fmName;
    }

    public Class<?> getJavaType() {
        return javaType;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FMAttribute)) {
            return false;
        }
        FMAttribute attribute = (FMAttribute) o;
        return javaType.equals(attribute.javaType) &&
                fieldName.equals(attribute.fieldName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(javaType, fieldName);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", FMAttribute.class.getSimpleName() + "[", "]")
                .add("javaType=" + javaType.getSimpleName())
                .add("name=" + fieldName)
                .toString();
    }
}
