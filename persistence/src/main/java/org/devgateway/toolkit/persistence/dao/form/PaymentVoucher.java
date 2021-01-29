package org.devgateway.toolkit.persistence.dao.form;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.devgateway.toolkit.persistence.dao.Form;
import org.devgateway.toolkit.persistence.excel.annotation.ExcelExport;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;

/**
 * @author mpostelnicu
 */
@Entity
@Audited
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(indexes = {@Index(columnList = "tender_process_id")})
@JsonInclude(JsonInclude.Include.NON_NULL)
@Form(featureName = "paymentVoucherForm")
public class PaymentVoucher extends AbstractImplTenderProcessMakueniEntity {

    @ExcelExport(useTranslation = true)
    private BigDecimal totalAmount;

    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToOne
    @ExcelExport(name = "Payment Vouchers PMC Reports", justExport = true)
    private PMCReport pmcReport;

    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToOne
    @ExcelExport(name = "Inspection Report", justExport = true)
    private InspectionReport inspectionReport;

    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToOne
    @ExcelExport(name = "Administrator Report", justExport = true)
    private AdministratorReport administratorReport;

    @ExcelExport(useTranslation = true)
    private Boolean lastPayment;

    @Override
    protected Collection<? extends AbstractMakueniEntity> getDirectChildrenEntities() {
        return Collections.emptyList();
    }

    @Override
    public void setLabel(String label) {

    }

    @Override
    public String getLabel() {
        return super.getLabel() + (totalAmount == null ? "" : " amount " + totalAmount);
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public PMCReport getPmcReport() {
        return pmcReport;
    }

    public void setPmcReport(PMCReport pmcReport) {
        this.pmcReport = pmcReport;
    }

    public InspectionReport getInspectionReport() {
        return inspectionReport;
    }

    public void setInspectionReport(InspectionReport inspectionReport) {
        this.inspectionReport = inspectionReport;
    }

    public AdministratorReport getAdministratorReport() {
        return administratorReport;
    }

    public void setAdministratorReport(AdministratorReport administratorReport) {
        this.administratorReport = administratorReport;
    }

    public Boolean getLastPayment() {
        return lastPayment;
    }

    public void setLastPayment(Boolean lastPayment) {
        this.lastPayment = lastPayment;
    }
}
