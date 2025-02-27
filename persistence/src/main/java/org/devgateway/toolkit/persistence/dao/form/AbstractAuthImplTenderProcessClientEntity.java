package org.devgateway.toolkit.persistence.dao.form;

import org.devgateway.toolkit.persistence.excel.annotation.ExcelExport;
import org.devgateway.toolkit.persistence.fm.service.DgFmService;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.AccessType;

import jakarta.persistence.MappedSuperclass;
import java.util.Collection;

@MappedSuperclass
@Audited
public abstract class AbstractAuthImplTenderProcessClientEntity extends AbstractImplTenderProcessClientEntity {

    @ExcelExport(useTranslation = true, name = "Authorize Payment")
    private Boolean authorizePayment;

    @Override
    @AccessType(AccessType.Type.PROPERTY)
    public String getLabel() {
        return super.getLabel() + (Boolean.TRUE.equals(authorizePayment) ? " (authorized)" : "");
    }

    @Override
    protected Collection<? extends AbstractClientEntity> getDirectChildrenEntities() {
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
