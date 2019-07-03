package org.devgateway.toolkit.persistence.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * @author idobre
 * @since 2019-04-05
 */
@MappedSuperclass
public abstract class AbstractChildAuditableEntity<P extends AbstractAuditableEntity> extends AbstractAuditableEntity {
    @ManyToOne
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinColumn(name = "parent_id", insertable = false, updatable = false)
    @JsonIgnore
    @org.springframework.data.annotation.Transient
    protected P parent;

    @Override
    @JsonIgnore
    @org.springframework.data.annotation.Transient
    public P getParent() {
        return parent;
    }

    public void setParent(final P parent) {
        this.parent = parent;
    }

}
