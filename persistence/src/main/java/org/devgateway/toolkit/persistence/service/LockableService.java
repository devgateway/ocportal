package org.devgateway.toolkit.persistence.service;

import org.devgateway.toolkit.persistence.dao.form.Lockable;

/**
 * @author Octavian Ciubotaru
 */
public interface LockableService<T extends Lockable> {

    void unlock(Long id);
}
