package org.devgateway.toolkit.persistence.dao.form;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.ArrayUtils;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.FileMetadata;
import org.devgateway.toolkit.persistence.dao.Form;
import org.devgateway.toolkit.persistence.dao.categories.Subcounty;
import org.devgateway.toolkit.persistence.dao.categories.Supplier;
import org.devgateway.toolkit.persistence.dao.categories.TargetGroup;
import org.devgateway.toolkit.persistence.dao.categories.Ward;
import org.devgateway.toolkit.persistence.excel.annotation.ExcelExport;
import org.devgateway.toolkit.persistence.spring.PersistenceUtil;
import org.devgateway.toolkit.persistence.validator.Severity;
import org.devgateway.toolkit.persistence.validator.groups.HighLevel;
import org.devgateway.toolkit.persistence.validator.validators.MaxAttachedFiles;
import org.devgateway.toolkit.persistence.validator.validators.UniqueTenderProcessEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author gmutuhu
 */
@Entity
@Audited
@Table(indexes = {@Index(columnList = "tender_process_id")},
        uniqueConstraints =
        @UniqueConstraint(columnNames = "tender_process_id"))
@JsonInclude(JsonInclude.Include.NON_NULL)
@Form(featureName = "contractForm")
@UniqueTenderProcessEntity(groups = HighLevel.class, payload = Severity.NonRecoverable.class,
        message = "{org.devgateway.toolkit.persistence.dao.form.UniqueContract.message}")
public class Contract extends AbstractTenderProcessClientEntity {
    @ExcelExport(useTranslation = true, name = "Contract Value")
    private BigDecimal contractValue;

    @ExcelExport(name = "Supplier")
    @ManyToOne
    private Supplier awardee;

    @ExcelExport(useTranslation = true, name = "Contract Date")
    private Date contractDate;

    @ExcelExport(useTranslation = true, name = "Contract Approved Date")
    private Date contractApprovalDate;

    @ExcelExport(useTranslation = true, name = "Expiry Date")
    private Date expiryDate;

    @ExcelExport(useTranslation = true, name = "Reference Number")
    @Column(length = DBConstants.STD_DEFAULT_TEXT_LENGTH)
    private String referenceNumber;

    @ExcelExport(useTranslation = true, name = "Description")
    @Column(length = DBConstants.STD_DEFAULT_TEXT_LENGTH)
    private String description;

    @ExcelExport(justExport = true, useTranslation = true, name = "Sub-Counties")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToMany
    private List<Subcounty> subcounties;

    @ExcelExport(justExport = true, useTranslation = true, name = "Wards")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToMany
    private List<Ward> wards = new ArrayList<>();

    @ExcelExport(separateSheet = true, useTranslation = true, name = "Contract Documents")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "parent_id")
    @OrderColumn(name = "index")
    private List<ContractDocument> contractDocs = new ArrayList<>();

    @ManyToOne
    @ExcelExport(justExport = true, useTranslation = true, name = "AGPO Category")
    private TargetGroup targetGroup;

    @ExcelExport(useTranslation = true, name = "Contract Extension Date")
    private Date contractExtensionDate;

    @ExcelExport(useTranslation = true, name = "Reason for Extension")
    @Column(length = DBConstants.MAX_DEFAULT_TEXT_LENGTH_ONE_LINE)
    private String reasonForExtension;

    public BigDecimal getContractValue() {
        return contractValue;
    }

    public void setContractValue(final BigDecimal contractValue) {
        this.contractValue = contractValue;
    }

    public Supplier getAwardee() {
        return awardee;
    }

    public void setAwardee(final Supplier awardee) {
        this.awardee = awardee;
    }

    public Date getContractApprovalDate() {
        return contractApprovalDate;
    }

    public void setContractApprovalDate(final Date contractApprovalDate) {
        this.contractApprovalDate = contractApprovalDate;
    }

    public Date getContractDate() {
        return contractDate;
    }

    public void setContractDate(final Date contractDate) {
        this.contractDate = contractDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(final Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(final String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public List<ContractDocument> getContractDocs() {
        return contractDocs;
    }

    public void setContractDocs(final List<ContractDocument> contractDocs) {
        this.contractDocs = contractDocs;
    }

    public TargetGroup getTargetGroup() {
        return targetGroup;
    }

    public void setTargetGroup(TargetGroup targetGroup) {
        this.targetGroup = targetGroup;
    }

    public Date getContractExtensionDate() {
        return contractExtensionDate;
    }

    public void setContractExtensionDate(Date contractExtensionDate) {
        this.contractExtensionDate = contractExtensionDate;
    }

    public String getReasonForExtension() {
        return reasonForExtension;
    }

    public void setReasonForExtension(String reasonForExtension) {
        this.reasonForExtension = reasonForExtension;
    }

    @Override
    public void setLabel(final String label) {
    }

    @Override
    @JsonIgnore
    @org.springframework.data.annotation.Transient
    public String getLabel() {
        return "Contract for tender process " + getTenderProcessNotNull().getLabel();
    }

    @JsonIgnore
    public boolean isTerminatedWithImplementation() {
        return PersistenceUtil.checkTerminated(
                ArrayUtils.add(getDirectChildrenEntities().toArray(new Statusable[]{}), this));
    }


    @Transactional
    @JsonIgnore
    @org.springframework.data.annotation.Transient
    @Override
    protected Collection<? extends AbstractClientEntity> getDirectChildrenEntities() {
        ArrayList<AbstractClientEntity> children = new ArrayList<>();
        children.addAll(getTenderProcessNotNull().getAdministratorReports());
        children.addAll(getTenderProcessNotNull().getPmcReports());
        children.addAll(getTenderProcessNotNull().getInspectionReports());
        children.addAll(getTenderProcessNotNull().getMeReports());
        children.addAll(getTenderProcessNotNull().getPaymentVouchers());
        return children;
    }

    @Override
    public Class<?> getNextForm() {
        return null;
    }

    @Override
    public boolean hasDownstreamForms() {
        return false;
    }

    public List<Subcounty> getSubcounties() {
        return subcounties;
    }

    public void setSubcounties(List<Subcounty> subcounties) {
        this.subcounties = subcounties;
    }

    public List<Ward> getWards() {
        return wards;
    }

    public void setWards(List<Ward> wards) {
        this.wards = wards;
    }

    @MaxAttachedFiles
    @JsonIgnore
    @org.springframework.data.annotation.Transient
    public Collection<FileMetadata> getAllAttachedFiles() {
        return contractDocs.stream()
                .flatMap(cd -> cd.getFormDocs().stream())
                .collect(Collectors.toCollection(HashSet::new));
    }
}
