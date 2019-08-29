package org.devgateway.toolkit.persistence.dao.alerts;

import org.devgateway.toolkit.persistence.dao.AbstractAuditableEntity;
import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.categories.Item;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author idobre
 * @since 2019-08-21
 */

// @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
// @Entity
// @Audited
// @Table(indexes = {@Index(columnList = "aaa"), @Index(columnList = "bbb")})
public class Alert extends AbstractAuditableEntity {
    private String email;

    private Boolean emailVerified = false;

    // use this flag to unsubscribe users
    private Boolean alertable = true;

    // TODO - check "creation date",
    private Date lastChecked;

    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Department> departments = new HashSet<>();

    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Item> items = new HashSet<>();

    @Override
    public AbstractAuditableEntity getParent() {
        return null;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(final Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public Boolean getAlertable() {
        return alertable;
    }

    public void setAlertable(final Boolean alertable) {
        this.alertable = alertable;
    }

    public Date getLastChecked() {
        return lastChecked;
    }

    public void setLastChecked(final Date lastChecked) {
        this.lastChecked = lastChecked;
    }

    public Set<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(final Set<Department> departments) {
        this.departments = departments;
    }

    public Set<Item> getItems() {
        return items;
    }

    public void setItems(final Set<Item> items) {
        this.items = items;
    }
}
