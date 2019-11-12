package org.devgateway.toolkit.persistence.dao.form;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.devgateway.toolkit.persistence.dao.AbstractChildAuditableEntity;
import org.devgateway.toolkit.persistence.dao.FileMetadata;
import org.devgateway.toolkit.persistence.dao.ListViewItem;
import org.devgateway.toolkit.persistence.dao.categories.ChargeAccount;
import org.devgateway.toolkit.persistence.dao.categories.Staff;
import org.devgateway.toolkit.persistence.excel.annotation.ExcelExport;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Audited
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(indexes = {@Index(columnList = "parent_id")})
public class PurchRequisition extends AbstractChildAuditableEntity<TenderProcess> implements ListViewItem {

    @ExcelExport(justExport = true, useTranslation = true, name = "Requested By")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToOne
    private Staff requestedBy;

    @ExcelExport(justExport = true, useTranslation = true, name = "Charge Account")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToOne
    private ChargeAccount chargeAccount;

    @ExcelExport(useTranslation = true, name = "Request Approval Date")
    private Date requestApprovalDate;

    @ExcelExport(name = "Purchase Items", separateSheet = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "parent_id")
    @OrderColumn(name = "index")
    private List<PurchaseItem> purchaseItems = new ArrayList<>();

    @ExcelExport(justExport = true, useTranslation = true, name = "Documents")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FileMetadata> formDocs;

    @Transient
    @JsonIgnore
    @org.springframework.data.annotation.Transient
    private Boolean expanded = false;

    @Override
    @JsonIgnore
    @org.springframework.data.annotation.Transient
    public Boolean getEditable() {
        return null;
    }

    @Override
    public void setEditable(final Boolean editable) {

    }

    @Override
    @JsonIgnore
    @org.springframework.data.annotation.Transient
    public Boolean getExpanded() {
        return expanded;
    }

    @Override
    public void setExpanded(final Boolean expanded) {
        this.expanded = expanded;
    }

    public Staff getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(Staff requestedBy) {
        this.requestedBy = requestedBy;
    }

    public ChargeAccount getChargeAccount() {
        return chargeAccount;
    }

    public void setChargeAccount(ChargeAccount chargeAccount) {
        this.chargeAccount = chargeAccount;
    }

    public Date getRequestApprovalDate() {
        return requestApprovalDate;
    }

    public void setRequestApprovalDate(Date requestApprovalDate) {
        this.requestApprovalDate = requestApprovalDate;
    }

    public List<PurchaseItem> getPurchaseItems() {
        return purchaseItems;
    }

    public void setPurchaseItems(List<PurchaseItem> purchaseItems) {
        this.purchaseItems = purchaseItems;
    }

    public Set<FileMetadata> getFormDocs() {
        return formDocs;
    }

    public void setFormDocs(Set<FileMetadata> formDocs) {
        this.formDocs = formDocs;
    }
}
