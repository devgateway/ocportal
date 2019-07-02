package org.devgateway.toolkit.persistence.dao.form;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.devgateway.toolkit.persistence.dao.DBConstants;

/**
 * @author mihai
 * <p>
 * Assigned to objects that provide a status, in our case, objects derived from
 * {@link org.devgateway.toolkit.persistence.dao.AbstractStatusAuditableEntity}
 */
public interface Statusable {

    String getStatus();

    /**
     * Default terminated scenario means the status was put to TERMINATED
     * @return
     */
    @JsonIgnore
    default boolean isTerminated() {
        return DBConstants.Status.TERMINATED.equals(getStatus());
    }

    /**
     * Determine if an Object can be exported to public portal, excel export, etc...
     */
    @JsonIgnore
    default boolean isExportable() {
        return DBConstants.Status.APPROVED.equals(getStatus())
                || DBConstants.Status.TERMINATED.equals(getStatus());
    }
}
