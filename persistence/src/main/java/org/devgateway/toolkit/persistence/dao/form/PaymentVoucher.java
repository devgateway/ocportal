package org.devgateway.toolkit.persistence.dao.form;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.devgateway.toolkit.persistence.dao.FileMetadata;
import org.devgateway.toolkit.persistence.dao.Form;
import org.devgateway.toolkit.persistence.excel.annotation.ExcelExport;
import org.devgateway.toolkit.persistence.validator.validators.MaxAttachedFiles;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author mpostelnicu
 */
@Entity
@Audited
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(indexes = {@Index(columnList = "tender_process_id")})
@JsonInclude(JsonInclude.Include.NON_NULL)
@Form(featureName = "paymentVoucherForm")
public class PaymentVoucher extends AbstractImplTenderProcessClientEntity {

    @ExcelExport(useTranslation = true)
    private BigDecimal totalAmount;

    @ManyToOne
    @ExcelExport(name = "Payment Vouchers PMC Reports", justExport = true)
    private PMCReport pmcReport;

    @ManyToOne
    @ExcelExport(name = "Inspection Report", justExport = true)
    private InspectionReport inspectionReport;

    @ManyToOne
    @ExcelExport(name = "Administrator Report", justExport = true)
    private AdministratorReport administratorReport;

    @ExcelExport(useTranslation = true)
    private Boolean lastPayment;

    @ExcelExport(justExport = true, useTranslation = true, name = "Completion Certificate")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FileMetadata> completionCertificate = new HashSet<>();

    @Override
    protected Collection<? extends AbstractClientEntity> getDirectChildrenEntities() {
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

    public Set<FileMetadata> getCompletionCertificate() {
        return completionCertificate;
    }

    public void setCompletionCertificate(Set<FileMetadata> completionCertificate) {
        this.completionCertificate = completionCertificate;
    }

    @MaxAttachedFiles
    @JsonIgnore
    @org.springframework.data.annotation.Transient
    public Collection<FileMetadata> getAllAttachedFiles() {
        return Stream.concat(
                getFormDocs().stream(),
                getCompletionCertificate().stream()
        ).collect(Collectors.toCollection(HashSet::new));
    }
}
