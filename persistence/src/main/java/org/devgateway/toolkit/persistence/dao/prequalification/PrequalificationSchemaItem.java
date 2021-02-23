package org.devgateway.toolkit.persistence.dao.prequalification;

import org.devgateway.toolkit.persistence.dao.AbstractChildExpandableAuditEntity;
import org.devgateway.toolkit.persistence.dao.Labelable;
import org.devgateway.toolkit.persistence.dao.ListViewItem;
import org.devgateway.toolkit.persistence.dao.categories.TargetGroup;
import org.devgateway.toolkit.persistence.validator.groups.NonDraft;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Audited
@Table(indexes = {@Index(columnList = "parent_id")})
public class PrequalificationSchemaItem extends AbstractChildExpandableAuditEntity<PrequalificationSchema>
        implements ListViewItem, Labelable {

    @NotNull(groups = NonDraft.class)
    private String code;

    @NotNull(groups = NonDraft.class)
    private String name;

    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToMany
    @Size(min = 1, groups = NonDraft.class, message =
      "{org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationSchemaItemCompanyCategories.message}")
    private List<TargetGroup> companyCategories = new ArrayList<>();

    @Override
    public void setLabel(String label) {

    }

    @Override
    public String getLabel() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TargetGroup> getCompanyCategories() {
        return companyCategories;
    }

    public void setCompanyCategories(List<TargetGroup> companyCategories) {
        this.companyCategories = companyCategories;
    }

    public String toString(PrequalificationYearRange yearRange) {
        return String.format("%s/%s/%s: %s", getParent().getPrefix(), yearRange.getName(), code, name);
    }
}