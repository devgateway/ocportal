package org.devgateway.toolkit.persistence.dao.prequalification;

import org.devgateway.toolkit.persistence.dao.AbstractAuditableEntity;
import org.devgateway.toolkit.persistence.dao.categories.Supplier;
import org.devgateway.toolkit.persistence.validator.groups.HighLevel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Octavian Ciubotaru
 */
@Entity
@Audited
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@UniquePrequalifiedSupplier(groups = HighLevel.class)
@UniquePrequalifiedSupplierItem
public class PrequalifiedSupplier extends AbstractAuditableEntity {

    @NotNull
    @ManyToOne
    private PrequalificationYearRange yearRange;

    @NotNull
    @ManyToOne
    private Supplier supplier;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, optional = false)
    private PrequalifiedSupplierContact contact = new PrequalifiedSupplierContact(this);

    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "parent_id")
    @OrderColumn(name = "index")
    private List<PrequalifiedSupplierItem> items = new ArrayList<>();

    @Override
    public AbstractAuditableEntity getParent() {
        return null;
    }

    public PrequalificationYearRange getYearRange() {
        return yearRange;
    }

    public void setYearRange(PrequalificationYearRange yearRange) {
        this.yearRange = yearRange;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public PrequalifiedSupplierContact getContact() {
        return contact;
    }

    public void setContact(PrequalifiedSupplierContact contact) {
        this.contact = contact;
    }

    public List<PrequalifiedSupplierItem> getItems() {
        return items;
    }

    public void setItems(List<PrequalifiedSupplierItem> items) {
        this.items = items;
    }
}
