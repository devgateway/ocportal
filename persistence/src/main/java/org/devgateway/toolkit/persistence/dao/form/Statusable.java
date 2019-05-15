package org.devgateway.toolkit.persistence.dao.form;

/**
 * @author mihai
 *
 * Assigned to objects that provide a status, in our case, objects derived from
 * {@link org.devgateway.toolkit.persistence.dao.AbstractStatusAuditableEntity}
 */
public interface Statusable {

    String getStatus();
}
