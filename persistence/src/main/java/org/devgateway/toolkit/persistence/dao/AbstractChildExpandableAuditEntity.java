package org.devgateway.toolkit.persistence.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Transient;

/**
 * @author mpostelnicu
 */
public abstract class AbstractChildExpandableAuditEntity<P extends AbstractAuditableEntity> extends
        AbstractChildAuditableEntity<P> implements ListViewItem {

    @Transient
    @JsonIgnore
    @org.springframework.data.annotation.Transient
    protected Boolean expanded = true;

    @Transient
    @JsonIgnore
    @org.springframework.data.annotation.Transient
    protected Boolean editable = true;

    @Override
    @JsonIgnore
    @org.springframework.data.annotation.Transient
    public Boolean getExpanded() {
        return expanded;
    }

    @Override
    public void setExpanded(final Boolean expanded) {
        this.expanded = expanded;
    }

    @Override
    @JsonIgnore
    @org.springframework.data.annotation.Transient
    public Boolean getEditable() {
        return null;
    }

    @Override
    public void setEditable(final Boolean editable) {
        this.editable = editable;
    }
}
