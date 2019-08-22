package org.devgateway.toolkit.persistence.dao.form;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.devgateway.toolkit.persistence.dao.AbstractChildAuditableEntity;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.Labelable;
import org.devgateway.toolkit.persistence.dao.ListViewItem;
import org.devgateway.toolkit.persistence.dao.categories.Item;
import org.devgateway.toolkit.persistence.dao.categories.ProcurementMethod;
import org.devgateway.toolkit.persistence.dao.categories.TargetGroup;
import org.devgateway.toolkit.persistence.excel.annotation.ExcelExport;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;

/**
 * @author idobre
 * @since 2019-04-05
 */
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Audited
@Table(indexes = {@Index(columnList = "parent_id"), @Index(columnList = "item_id")})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlanItem extends AbstractChildAuditableEntity<ProcurementPlan> implements ListViewItem, Labelable {
    @ExcelExport(justExport = true, useTranslation = true, name = "Item")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToOne
    private Item item;

    @ExcelExport(useTranslation = true, name = "Estimated Cost")
    private BigDecimal estimatedCost;

    @ExcelExport(useTranslation = true, name = "Unit Of Issue")
    @Column(length = DBConstants.STD_DEFAULT_TEXT_LENGTH)
    private String unitOfIssue;

    @ExcelExport(useTranslation = true, name = "Quantity")
    private BigDecimal quantity;

    @ExcelExport(useTranslation = true, name = "Unit Price")
    private BigDecimal unitPrice;

    @ExcelExport(useTranslation = true, name = "Total Cost")
    private BigDecimal totalCost;

    @ExcelExport(justExport = true, useTranslation = true, name = "Procurement Method")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToOne
    private ProcurementMethod procurementMethod;

    @ExcelExport(useTranslation = true, name = "Source of Funds")
    @Column(length = DBConstants.STD_DEFAULT_TEXT_LENGTH)
    private String sourceOfFunds;

    @ExcelExport(justExport = true, useTranslation = true, name = "Target Group")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToOne
    private TargetGroup targetGroup;

    @ExcelExport(useTranslation = true, name = "Target Group Value")
    private BigDecimal targetGroupValue;

    @ExcelExport(useTranslation = true, name = "1st Quarter")
    private BigDecimal quarter1st;

    @ExcelExport(useTranslation = true, name = "2nd Quarter")
    private BigDecimal quarter2nd;

    @ExcelExport(useTranslation = true, name = "3rd Quarter")
    private BigDecimal quarter3rd;

    @ExcelExport(useTranslation = true, name = "4th Quarter")
    private BigDecimal quarter4th;

    @Transient
    @JsonIgnore
    @org.springframework.data.annotation.Transient
    private Boolean editable = false;

    @Transient
    @JsonIgnore
    @org.springframework.data.annotation.Transient
    private Boolean expanded = false;

    public Item getItem() {
        return item;
    }

    public void setItem(final Item item) {
        this.item = item;
    }

    public BigDecimal getEstimatedCost() {
        return estimatedCost;
    }

    public void setEstimatedCost(final BigDecimal estimatedCost) {
        this.estimatedCost = estimatedCost;
    }

    public String getUnitOfIssue() {
        return unitOfIssue;
    }

    public void setUnitOfIssue(final String unitOfIssue) {
        this.unitOfIssue = unitOfIssue;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(final BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(final BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(final BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public ProcurementMethod getProcurementMethod() {
        return procurementMethod;
    }

    public void setProcurementMethod(final ProcurementMethod procurementMethod) {
        this.procurementMethod = procurementMethod;
    }

    public String getSourceOfFunds() {
        return sourceOfFunds;
    }

    public void setSourceOfFunds(final String sourceOfFunds) {
        this.sourceOfFunds = sourceOfFunds;
    }

    public TargetGroup getTargetGroup() {
        return targetGroup;
    }

    public void setTargetGroup(final TargetGroup targetGroup) {
        this.targetGroup = targetGroup;
    }

    public BigDecimal getTargetGroupValue() {
        return targetGroupValue;
    }

    public void setTargetGroupValue(final BigDecimal targetGroupValue) {
        this.targetGroupValue = targetGroupValue;
    }

    public BigDecimal getQuarter1st() {
        return quarter1st;
    }

    public void setQuarter1st(final BigDecimal quarter1st) {
        this.quarter1st = quarter1st;
    }

    public BigDecimal getQuarter2nd() {
        return quarter2nd;
    }

    public void setQuarter2nd(final BigDecimal quarter2nd) {
        this.quarter2nd = quarter2nd;
    }

    public BigDecimal getQuarter3rd() {
        return quarter3rd;
    }

    public void setQuarter3rd(final BigDecimal quarter3rd) {
        this.quarter3rd = quarter3rd;
    }

    public BigDecimal getQuarter4th() {
        return quarter4th;
    }

    public void setQuarter4th(final BigDecimal quarter4th) {
        this.quarter4th = quarter4th;
    }

    @Override
    @JsonIgnore
    @org.springframework.data.annotation.Transient
    public Boolean getEditable() {
        return editable;
    }

    @Override
    public void setEditable(final Boolean editable) {
        this.editable = editable;
    }

    @Override
    @JsonIgnore
    @org.springframework.data.annotation.Transient
    public Boolean getExpanded() {
        return expanded;
    }

    @Override
    public void setExpanded(final Boolean expanded) {
        this.expanded = expanded;
    }

    @Override
    public void setLabel(final String label) {

    }

    @Override
    @JsonIgnore
    @org.springframework.data.annotation.Transient
    public String getLabel() {
        return item != null ? item.getLabel() : "";
    }

    @Override
    public String toString() {
        return getLabel();
    }
}
