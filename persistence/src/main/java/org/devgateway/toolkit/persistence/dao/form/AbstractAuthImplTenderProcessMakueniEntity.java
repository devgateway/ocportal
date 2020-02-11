package org.devgateway.toolkit.persistence.dao.form;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractAuthImplTenderProcessMakueniEntity extends AbstractImplTenderProcessMakueniEntity {

    private Boolean authorizePayment;

    @Override
    public String getLabel() {
        return super.getLabel() + (authorizePayment ? " (authorized)" : "");
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
