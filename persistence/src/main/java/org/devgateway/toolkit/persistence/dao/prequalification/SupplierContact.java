package org.devgateway.toolkit.persistence.dao.prequalification;

import org.devgateway.toolkit.persistence.dao.categories.Supplier;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import jakarta.persistence.Entity;

/**
 * @author Octavian Ciubotaru
 */
@Entity
@Audited
public class SupplierContact extends AbstractContact<Supplier> {

    public SupplierContact() {
    }

    public SupplierContact(AbstractContact<?> copy) {
        copy(copy, this);
    }

    public SupplierContact(Supplier supplier) {
        setParent(supplier);
    }
}
