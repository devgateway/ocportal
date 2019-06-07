package org.devgateway.toolkit.persistence.dao.form;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.devgateway.toolkit.persistence.dao.AbstractChildAuditableEntity;
import org.devgateway.toolkit.persistence.dao.ListViewItem;
import org.devgateway.toolkit.persistence.excel.annotation.ExcelExport;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author gmutuhu
 */

@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Audited
@Table(indexes = {@Index(columnList = "parent_id")})
public class TenderItem extends AbstractChildAuditableEntity<Tender> implements ListViewItem {
    @ExcelExport(justExport = true, useTranslation = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToOne
    private PurchaseItem purchaseItem;

    @ExcelExport(useTranslation = true)
    private String unitOfIssue;

    @ExcelExport(useTranslation = true)
    private Integer quantity;

    @ExcelExport(useTranslation = true)
    private Double unitPrice;

    @ExcelExport(useTranslation = true)
    private Double totalCost;

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

    public PurchaseItem getPurchaseItem() {
        return purchaseItem;
    }

    public void setPurchaseItem(final PurchaseItem purchaseItem) {
        this.purchaseItem = purchaseItem;
    }

    @Transient
    @JsonIgnore
    private Boolean expanded = false;

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

}
