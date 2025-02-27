package org.devgateway.toolkit.persistence.dao.categories;

import org.devgateway.toolkit.persistence.dao.AbstractAuditableEntity;
import org.hibernate.envers.Audited;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * @author mpostelnicu
 */
@Entity
@Audited
public class SubWard extends LocationPointCategory {
    @ManyToOne
    @JoinColumn(name = "ward_id")
    private Ward ward;

    public Ward getWard() {
        return ward;
    }

    public void setWard(Ward ward) {
        this.ward = ward;
    }

    @Override
    public AbstractAuditableEntity getParent() {
        return ward;
    }
}
