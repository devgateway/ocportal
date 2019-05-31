package org.devgateway.toolkit.persistence.dao.form;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@MappedSuperclass
public abstract class AbstractPurchaseReqMakueniEntity extends AbstractMakueniEntity implements ProjectAttachable,
        ProcurementPlanAttachable {
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_requisition_id")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    @NotNull
    protected PurchaseRequisition purchaseRequisition;

    public PurchaseRequisition getPurchaseRequisition() {
        return purchaseRequisition;
    }

    public void setPurchaseRequisition(final PurchaseRequisition purchaseRequisition) {
        this.purchaseRequisition = purchaseRequisition;
    }

    @Override
    public Project getProject() {
        return getPurchaseRequisition().getProject();
    }

    @Override
    public ProcurementPlan getProcurementPlan() {
        if (purchaseRequisition != null && purchaseRequisition.getProject() != null) {
            return purchaseRequisition.getProject().getProcurementPlan();
        }
        return null;
    }
}
