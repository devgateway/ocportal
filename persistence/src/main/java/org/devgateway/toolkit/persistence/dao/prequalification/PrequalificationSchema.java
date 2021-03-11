package org.devgateway.toolkit.persistence.dao.prequalification;

import org.devgateway.toolkit.persistence.dao.AbstractAuditableEntity;
import org.devgateway.toolkit.persistence.dao.AbstractStatusAuditableEntity;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.Form;
import org.devgateway.toolkit.persistence.dao.Labelable;
import org.devgateway.toolkit.persistence.validator.groups.NonDraft;
import org.devgateway.toolkit.persistence.validator.validators.UniquePrequalificationSchema;
import org.devgateway.toolkit.persistence.validator.validators.UniquePrequalificationSchemaItems;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Audited
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(indexes = {@Index(columnList = "name")})
@Form(featureName = "prequalificationSchemaForm")
@UniquePrequalificationSchema(groups = NonDraft.class,
        message = "{org.devgateway.toolkit.persistence.dao.prequalification.UniquePrequalificationSchema.message}")
@UniquePrequalificationSchemaItems(groups = NonDraft.class,
        message = "{org.devgateway.toolkit.persistence.dao.prequalification.UniquePrequalificationSchemaItems.message}")
public class PrequalificationSchema extends AbstractStatusAuditableEntity implements Labelable {

    @NotNull(groups = NonDraft.class)
    private String name;

    @NotNull(groups = NonDraft.class)
    private String prefix;

    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(mappedBy = "schema")
    private Set<PrequalificationYearRange> prequalificationYearRanges = new HashSet<>();

    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "parent_id")
    @OrderColumn(name = "index")
    @Size(min = 1, groups = NonDraft.class, message =
    "{org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationSchemaItemsSize.message}")
    private List<PrequalificationSchemaItem> items = new ArrayList<>();

    @Override
    public AbstractAuditableEntity getParent() {
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public List<PrequalificationSchemaItem> getItems() {
        return items;
    }

    public void setItems(List<PrequalificationSchemaItem> items) {
        this.items = items;
    }

    public Set<PrequalificationYearRange> getPrequalificationYearRanges() {
        return prequalificationYearRanges;
    }

    public void setPrequalificationYearRanges(Set<PrequalificationYearRange> prequalificationYearRanges) {
        this.prequalificationYearRanges = prequalificationYearRanges;
    }

    @Override
    public void setLabel(String label) {
        this.name = label;
    }

    @Override
    public String getLabel() {
        return name;
    }

    @Override
    public String toString() {
        return getLabel();
    }

    public boolean isSelectable() {
        return DBConstants.Status.SUBMITTED.equals(getStatus());
    }
}
