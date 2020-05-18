package org.devgateway.toolkit.persistence.spring;

import org.devgateway.toolkit.persistence.dao.GenericPersistable;
import org.devgateway.toolkit.persistence.dao.form.Statusable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
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
    @Transactional
    public static <T extends GenericPersistable> T getNext(Set<T> set) {
        if (set == null || set.isEmpty()) {
            return null;
        }
        return set.iterator().next();
    }

    public static ZonedDateTime convertDateToZonedDateTime(Date date) {
        return date == null ? null : date.toInstant().atZone(ZoneId.systemDefault());
    }

    public static ZonedDateTime currentDate() {
        return LocalDateTime.now().atZone(ZoneId.systemDefault());
    }

}
