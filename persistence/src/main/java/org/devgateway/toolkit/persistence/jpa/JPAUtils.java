package org.devgateway.toolkit.persistence.jpa;

import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @author Octavian Ciubotaru
 */
public final class JPAUtils {

    private JPAUtils() {
    }

    /**
     * Is the attribute represented by this field persisted by JPA?
     */
    public static boolean isJPAPersistedField(Field field) {
        return !field.isAnnotationPresent(Transient.class)
                && !Modifier.isTransient(field.getModifiers())
                && !Modifier.isStatic(field.getModifiers());
    }
}
