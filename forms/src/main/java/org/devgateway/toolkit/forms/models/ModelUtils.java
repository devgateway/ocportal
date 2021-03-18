package org.devgateway.toolkit.forms.models;

import org.danekja.java.util.function.serializable.SerializableFunction;
import org.devgateway.toolkit.persistence.dao.GenericPersistable;
import org.devgateway.toolkit.persistence.service.BaseJpaService;

/**
 * @author Octavian Ciubotaru
 */
public final class ModelUtils {

    private ModelUtils() {
    }

    /**
     * Function that will try to load and attach to hibernate session the entity passed as the parameter.
     *
     * <p>Useful when working with versioned entities with lazily-loaded fields.</p>
     *
     * <p>Example:<br><code>
     *     getModel().map(fromSession(userService)).map(User::getTheLazyField)
     * </code></p>
     */
    public static <Y extends GenericPersistable> SerializableFunction<Y, Y> fromSession(BaseJpaService<Y> service) {
        return e -> service.findById(e.getId()).orElse(null);
    }
}
