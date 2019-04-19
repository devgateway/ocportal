package org.devgateway.toolkit.persistence.dao.form;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.devgateway.toolkit.persistence.dao.AbstractChildAuditableEntity;
import org.devgateway.toolkit.persistence.dao.ListViewItem;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author gmutuhu
 *
 */

@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Audited
@Table(indexes = {@Index(columnList = "parent_id"), @Index(columnList = "item_detail_id"), @Index(columnList = "description")})
public class TenderItem extends AbstractChildAuditableEntity<Tender> implements ListViewItem {
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToOne
    private ItemDetail itemDetail; //requisition item
    private String description;
    private String unitOfIssue;
    private Integer quantity;
    private Double unitPrice;    
    private Double totalCost;
    
    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getUnitOfIssue() {
        return unitOfIssue;
    }

    public void setUnitOfIssue(final String unitOfIssue) {
        this.unitOfIssue = unitOfIssue;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(final Integer quantity) {
        this.quantity = quantity;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(final Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(final Double totalCost) {
        this.totalCost = totalCost;
    }

    public ItemDetail getItemDetail() {
        return itemDetail;
    }

    public void setItemDetail(ItemDetail itemDetail) {
        this.itemDetail = itemDetail;
    }

    @Transient
    @JsonIgnore
    private Boolean editable = false;

    @Transient
    @JsonIgnore
    private Boolean expanded = false;

    @Override
    public Boolean getEditable() {
        return editable;
    }

    @Override
    public void setEditable(final Boolean editable) {
        this.editable = editable;        
    }

    @Override
    public Boolean getExpanded() {
        return expanded;
    }

    @Override
    public void setExpanded(final Boolean expanded) {
        this.expanded = expanded;        
    }

}
