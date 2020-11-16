package org.devgateway.toolkit.persistence.jpa;

import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.metamodel.Attribute;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;

/**
 * Represents a JPA attribute of a java type.
 *
 * Very similar to {@link javax.persistence.metamodel.Attribute}.
 */
public class JPAAttribute {

    private final Field field;

    private final Attribute.PersistentAttributeType type;

    public JPAAttribute(Field field) {
        this.field = field;

        if (field.isAnnotationPresent(OneToOne.class)) {
            type = Attribute.PersistentAttributeType.ONE_TO_ONE;
        } else if (field.isAnnotationPresent(OneToMany.class)) {
            type = Attribute.PersistentAttributeType.ONE_TO_MANY;
        } else if (field.isAnnotationPresent(ManyToOne.class)) {
            type = Attribute.PersistentAttributeType.MANY_TO_ONE;
        } else if (field.isAnnotationPresent(ManyToMany.class)) {
            type = Attribute.PersistentAttributeType.MANY_TO_MANY;
        } else if (field.isAnnotationPresent(Embedded.class)) {
            type = Attribute.PersistentAttributeType.EMBEDDED;
        } else if (field.isAnnotationPresent(ElementCollection.class)) {
            type = Attribute.PersistentAttributeType.ELEMENT_COLLECTION;
        } else {
            type = Attribute.PersistentAttributeType.BASIC;
        }
    }

    public String getName() {
        return field.getName();
    }

    /**
     * Is a collection or array.
     */
    public boolean isCollection() {
        return Collection.class.isAssignableFrom(field.getType())
                || field.getType().isArray();
    }

    // TODO may not work for arrays
    public Class<?> getElementType() {
        ParameterizedType type = (ParameterizedType) field.getGenericType();
        return (Class<?>) type.getActualTypeArguments()[0];
    }

    public Class<?> getDeclaringType() {
        return field.getDeclaringClass();
    }

    public boolean isOneToMany() {
        return type == Attribute.PersistentAttributeType.ONE_TO_MANY;
    }

    public boolean isOwningOneToMany() {
        OneToMany oneToMany = field.getAnnotation(OneToMany.class);
        return oneToMany.mappedBy().isEmpty();
    }

    public boolean isOneToOne() {
        return type == Attribute.PersistentAttributeType.ONE_TO_ONE;
    }

    public boolean isManyToOne() {
        return type == Attribute.PersistentAttributeType.MANY_TO_ONE;
    }

    public boolean isManyToMany() {
        return type == Attribute.PersistentAttributeType.MANY_TO_MANY;
    }

    public boolean isBasic() {
        return type == Attribute.PersistentAttributeType.BASIC;
    }

    public Class<?> getJavaType() {
        return field.getType();
    }
}
