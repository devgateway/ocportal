package org.devgateway.toolkit.persistence.dao.form;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.devgateway.toolkit.persistence.dao.AbstractChildAuditableEntity;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.Labelable;
import org.devgateway.toolkit.persistence.dao.ListViewItem;
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
 * @since 2019-04-17
 */
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Audited
@Table(indexes = {@Index(columnList = "parent_id"),
        @Index(columnList = "plan_item_id"),
        @Index(columnList = "description")})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PurchaseItem extends AbstractChildAuditableEntity<PurchaseRequisition> implements ListViewItem, Labelable {
    @ExcelExport(justExport = true, useTranslation = true, name = "Item")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToOne
    private PlanItem planItem;

    @ExcelExport(useTranslation = true, name = "Description")
    @Column(length = DBConstants.MAX_DEFAULT_TEXT_LENGTH_ONE_LINE)
    private String description;

    @ExcelExport(useTranslation = true, name = "Quantity")
    private BigDecimal quantity;

    @ExcelExport(useTranslation = true, name = "Unit of Issue")
    @Column(length = DBConstants.STD_DEFAULT_TEXT_LENGTH)
    private String unit;

    @ExcelExport(useTranslation = true, name = "Unit Price")
    private BigDecimal amount;

    @Transient
    @JsonIgnore
    @org.springframework.data.annotation.Transient
    private Boolean expanded = false;

    public PlanItem getPlanItem() {
        return planItem;
    }

    public void setPlanItem(final PlanItem planItem) {
        this.planItem = planItem;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(final BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(final String unit) {
        this.unit = unit;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(final BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    @Override
    @JsonIgnore
    @org.springframework.data.annotation.Transient
    public Boolean getEditable() {
        return null;
    }

    @Override
    public void setEditable(final Boolean editable) {

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
    @JsonIgnore
    @org.springframework.data.annotation.Transient
    public String getLabel() {
        return planItem != null ? planItem.getLabel() : "";
    }

    @Override
    public String toString() {
        return getLabel();
    }

    @Override
    public void setLabel(final String label) {

    }
}
