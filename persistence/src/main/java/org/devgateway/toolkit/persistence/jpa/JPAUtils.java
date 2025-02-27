package org.devgateway.toolkit.persistence.jpa;

import jakarta.persistence.OneToMany;
import jakarta.persistence.metamodel.Attribute;
import java.lang.reflect.Field;

/**
 * @author Octavian Ciubotaru
 */
public final class JPAUtils {

    private JPAUtils() {
    }

    public static boolean isBasic(Attribute<?, ?> attr) {
        return attr.getPersistentAttributeType() == Attribute.PersistentAttributeType.BASIC;
    }

    public static boolean isManyToMany(Attribute<?, ?> attr) {
        return attr.getPersistentAttributeType() == Attribute.PersistentAttributeType.MANY_TO_MANY;
    }

    public static boolean isManyToOne(Attribute<?, ?> attr) {
        return attr.getPersistentAttributeType() == Attribute.PersistentAttributeType.MANY_TO_ONE;
    }

    public static boolean isOneToOne(Attribute<?, ?> attr) {
        return attr.getPersistentAttributeType() == Attribute.PersistentAttributeType.ONE_TO_ONE;
    }

    public static boolean isOneToMany(Attribute<?, ?> attr) {
        return attr.getPersistentAttributeType() == Attribute.PersistentAttributeType.ONE_TO_MANY;
    }

    public static boolean isOwningOneToMany(Attribute<?, ?> attr) {
        OneToMany oneToMany = ((Field) attr.getJavaMember()).getAnnotation(OneToMany.class);
        return oneToMany != null && oneToMany.mappedBy().isEmpty();
    }
}
