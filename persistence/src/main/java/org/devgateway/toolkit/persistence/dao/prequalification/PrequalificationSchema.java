package org.devgateway.toolkit.persistence.dao.prequalification;

import org.devgateway.toolkit.persistence.dao.AbstractAuditableEntity;
import org.devgateway.toolkit.persistence.dao.AbstractStatusAuditableEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Audited
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(indexes = {@Index(columnList = "name")})
public class PrequalificationSchema extends AbstractStatusAuditableEntity {

    private String name;

    private String prefix;

    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(mappedBy = "schema")
    private Set<PrequalificationYearRange> prequalificationYearRanges = new HashSet<>();

    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "parent_id")
    @OrderColumn(name = "index")
    @Size(min = 1)
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
}
