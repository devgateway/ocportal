package org.devgateway.toolkit.persistence.dao.form;

import javax.persistence.MappedSuperclass;
import java.util.Collection;

@MappedSuperclass
public abstract class AbstractAuthImplTenderProcessMakueniEntity extends AbstractImplTenderProcessMakueniEntity {

    private Boolean authorizePayment;

    @Override
    public String getLabel() {
        return super.getLabel() + (Boolean.TRUE.equals(authorizePayment) ? " (authorized)" : "");
    }

    @Override
    public Collection<? extends AbstractMakueniEntity> getDirectChildrenEntities() {
        return getTenderProcessNotNull().getPaymentVouchers();
    }

    @Override
    public void setLabel(String label) {

    }

    public Boolean getAuthorizePayment() {
        return authorizePayment;
    }

    public void setAuthorizePayment(Boolean authorizePayment) {
        this.authorizePayment = authorizePayment;
    }
}
