package org.devgateway.toolkit.persistence.dao.prequalification;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import jakarta.persistence.Entity;

/**
 * @author Octavian Ciubotaru
 */
@Entity
@Audited
public class PrequalifiedSupplierContact extends AbstractContact<PrequalifiedSupplier> {

    public PrequalifiedSupplierContact() {
    }

    public PrequalifiedSupplierContact(PrequalifiedSupplier prequalifiedSupplier) {
        setParent(prequalifiedSupplier);
    }
}
