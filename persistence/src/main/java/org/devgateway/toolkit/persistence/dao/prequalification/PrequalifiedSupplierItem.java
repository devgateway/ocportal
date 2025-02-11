package org.devgateway.toolkit.persistence.dao.prequalification;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.devgateway.toolkit.persistence.dao.AbstractChildExpandableAuditEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;

/**
 * @author Octavian Ciubotaru
 */
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Audited
@Table(indexes = {@Index(columnList = "parent_id")},
        uniqueConstraints = {@UniqueConstraint(columnNames = { "parent_id", "item_id" })})
public class PrequalifiedSupplierItem extends AbstractChildExpandableAuditEntity<PrequalifiedSupplier> {

    @NotNull
    @ManyToOne
    private PrequalificationSchemaItem item;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private PrequalifiedSupplierItemContact contact;

    public PrequalificationSchemaItem getItem() {
        return item;
    }

    public void setItem(PrequalificationSchemaItem item) {
        this.item = item;
    }

    public PrequalifiedSupplierItemContact getContact() {
        return contact;
    }

    public void setContact(PrequalifiedSupplierItemContact contact) {
        this.contact = contact;
    }

    @JsonIgnore
    public AbstractContact<?> getNonNullContact() {
        if (contact == null) {
            return getParent().getContact();
        }
        return contact;
    }
}
