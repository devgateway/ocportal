package org.devgateway.toolkit.persistence.dao.alerts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.devgateway.toolkit.persistence.dao.AbstractAuditableEntity;
import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.categories.Item;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * @author idobre
 * @since 2019-08-21
 */
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Audited
@Table(indexes = {@Index(columnList = "email"),
        @Index(columnList = "emailVerified"),
        @Index(columnList = "alertable"),
        @Index(columnList = "secret"),
        @Index(columnList = "failCount"),
        @Index(columnList = "lastChecked")})
public class Alert extends AbstractAuditableEntity {
    private String email;

    private Boolean emailVerified = false;

    // use this flag to unsubscribe users
    private Boolean alertable = true;

    @JsonIgnore
    private String secret;

    @JsonIgnore
    private LocalDateTime secretValidUntil;

    @JsonIgnore
    private Integer failCount;

    private LocalDateTime lastChecked;

    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToMany
    private Set<Department> departments = new HashSet<>();

    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToMany
    private Set<Item> items = new HashSet<>();

    public Alert() {

    }

    public Alert(final String email, final Set<Department> departments, final Set<Item> items) {
        this.email = email;
        this.departments = new HashSet<>(departments);
        this.items = new HashSet<>(items);
        this.lastChecked = LocalDateTime.now();
    }

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

    public String getSecret() {
        return secret;
    }

    public void setSecret(final String secret) {
        this.secret = secret;
    }

    public LocalDateTime getSecretValidUntil() {
        return secretValidUntil;
    }

    public void setSecretValidUntil(final LocalDateTime secretValidUntil) {
        this.secretValidUntil = secretValidUntil;
    }

    public Integer getFailCount() {
        return failCount;
    }

    public void setFailCount(final Integer failCount) {
        this.failCount = failCount;
    }

    public LocalDateTime getLastChecked() {
        return lastChecked;
    }

    public void setLastChecked(final LocalDateTime lastChecked) {
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
