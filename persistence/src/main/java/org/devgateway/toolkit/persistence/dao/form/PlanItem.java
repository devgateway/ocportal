package org.devgateway.toolkit.persistence.dao.form;

import org.devgateway.toolkit.persistence.dao.AbstractChildAuditableEntity;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.categories.Item;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author idobre
 * @since 2019-04-05
 */
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Audited
@Table(indexes = {@Index(columnList = "parent_id")})
public class PlanItem extends AbstractChildAuditableEntity<ProcurementPlan> {
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToOne
    private Item item;

    @Column(length = DBConstants.MAX_DEFAULT_TEXT_LENGTH_ONE_LINE)
    private String description;

    private Long estimatedCost;

    @Column(length = DBConstants.STD_DEFAULT_TEXT_LENGTH)
    private String unitOfIssue;

    private Integer quantity;

    private Integer unitPrice;

    private Long totalCost;

    public Item getItem() {
        return item;
    }

    public void setItem(final Item item) {
        this.item = item;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Long getEstimatedCost() {
        return estimatedCost;
    }

    public void setEstimatedCost(final Long estimatedCost) {
        this.estimatedCost = estimatedCost;
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

    public Integer getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(final Integer unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Long getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(final Long totalCost) {
        this.totalCost = totalCost;
    }
}
