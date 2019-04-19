package org.devgateway.toolkit.persistence.dao.form;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.devgateway.toolkit.persistence.dao.AbstractChildAuditableEntity;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.Labelable;
import org.devgateway.toolkit.persistence.dao.ListViewItem;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author idobre
 * @since 2019-04-17
 */
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Audited
@Table(indexes = {@Index(columnList = "parent_id")})
public class ItemDetail extends AbstractChildAuditableEntity<PurchaseRequisition> implements ListViewItem, Labelable {
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToOne
    private PlanItem planItem;

    private Integer quantity;

    @Column(length = DBConstants.STD_DEFAULT_TEXT_LENGTH)
    private String unit;

    private Double amount;

    @Transient
    @JsonIgnore
    private Boolean expanded = false;

    public PlanItem getPlanItem() {
        return planItem;
    }

    public void setPlanItem(final PlanItem planItem) {
        this.planItem = planItem;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(final Integer quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(final String unit) {
        this.unit = unit;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(final Double amount) {
        this.amount = amount;
    }

    @Override
    public Boolean getEditable() {
        return null;
    }

    @Override
    public void setEditable(final Boolean editable) {

    }

    @Override
    public Boolean getExpanded() {
        return expanded;
    }

    @Override
    public void setExpanded(final Boolean expanded) {
        this.expanded = expanded;
    }
    
    @Override
    public String getLabel() {
        return planItem.getDescription();
    }

    @Override
    public String toString() {
        return getLabel();
    }

    @Override
    public void setLabel(String label) {
               
    }
}
