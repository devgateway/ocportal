package org.devgateway.toolkit.persistence.dao;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OrderColumn;

/**
 * @author idobre
 * @since 2019-04-05
 */
@MappedSuperclass
public abstract class AbstractChildAuditableEntity<P extends AbstractAuditableEntity> extends AbstractAuditableEntity {
    @ManyToOne
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OrderColumn(name = "index")
    @JoinColumn(name = "parent_id", insertable = false, updatable = false)
    protected P parent;

    @Override
    public P getParent() {
        return parent;
    }

    public void setParent(final P parent) {
        this.parent = parent;
    }

}
