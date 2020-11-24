package org.devgateway.toolkit.persistence.dao.form;

import org.devgateway.toolkit.persistence.excel.annotation.ExcelExport;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.AccessType;

import javax.persistence.MappedSuperclass;
import java.util.Collection;

@MappedSuperclass
@Audited
public abstract class AbstractAuthImplTenderProcessMakueniEntity extends AbstractImplTenderProcessMakueniEntity {

    @ExcelExport(useTranslation = true, name = "Authorize Payment")
    private Boolean authorizePayment;

    @Override
    @AccessType(AccessType.Type.PROPERTY)
    public String getLabel() {
        return super.getLabel() + (Boolean.TRUE.equals(authorizePayment) ? " (authorized)" : "");
    }

    @Override
    protected Collection<? extends AbstractMakueniEntity> getDirectChildrenEntities() {
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
