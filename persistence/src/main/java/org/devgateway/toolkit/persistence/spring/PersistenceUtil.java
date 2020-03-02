package org.devgateway.toolkit.persistence.spring;

import org.devgateway.toolkit.persistence.dao.GenericPersistable;
import org.devgateway.toolkit.persistence.dao.form.Statusable;

import java.util.Set;

public final class PersistenceUtil {

    private PersistenceUtil() {

    }

    public static boolean checkTerminated(Statusable... statusables) {
        for (Statusable statusable : statusables) {
            if (statusable != null && statusable.isTerminated()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the next element in a set, returns element or null if null or empty set
     *
     * @param set
     * @param <T>
     * @return
     */
    public static <T extends GenericPersistable> T getNext(Set<T> set) {
        if (set == null || set.isEmpty()) {
            return null;
        }
        return set.iterator().next();
    }

}
