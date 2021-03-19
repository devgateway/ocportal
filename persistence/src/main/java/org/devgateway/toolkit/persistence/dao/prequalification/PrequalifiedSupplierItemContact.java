package org.devgateway.toolkit.persistence.dao.prequalification;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;

/**
 * @author Octavian Ciubotaru
 */
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Audited
public class PrequalifiedSupplierItemContact extends AbstractContact<PrequalifiedSupplierItem> {

    public PrequalifiedSupplierItemContact() {
    }

    public PrequalifiedSupplierItemContact(PrequalifiedSupplierItem item) {
        setParent(item);
    }
}
