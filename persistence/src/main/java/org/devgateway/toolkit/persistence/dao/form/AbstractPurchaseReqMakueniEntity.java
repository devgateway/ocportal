package org.devgateway.toolkit.persistence.dao.form;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.util.Objects;

@MappedSuperclass
public abstract class AbstractPurchaseReqMakueniEntity extends AbstractMakueniEntity implements ProjectAttachable,
        ProcurementPlanAttachable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_requisition_id")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    @org.springframework.data.annotation.Transient
    protected PurchaseRequisition purchaseRequisition;

    @JsonIgnore
    @org.springframework.data.annotation.Transient
    public PurchaseRequisition getPurchaseRequisition() {
        return purchaseRequisition;
    }

    public void setPurchaseRequisition(final PurchaseRequisition purchaseRequisition) {
        this.purchaseRequisition = purchaseRequisition;
    }

    @Override
    @JsonIgnore
    @org.springframework.data.annotation.Transient
    public Project getProject() {
        return getPurchaseRequisition().getProject();
    }

    @JsonIgnore
    @org.springframework.data.annotation.Transient
    public PurchaseRequisition getPurchaseRequisitionNotNull() {
        Objects.requireNonNull(purchaseRequisition, "Purchase requisition must not be null at this stage!");
        return purchaseRequisition;
    }

    @Override
    @JsonIgnore
    @org.springframework.data.annotation.Transient
    public ProcurementPlan getProcurementPlan() {
        if (purchaseRequisition != null && purchaseRequisition.getProject() != null) {
            return purchaseRequisition.getProject().getProcurementPlan();
        }
        return null;
    }
}
