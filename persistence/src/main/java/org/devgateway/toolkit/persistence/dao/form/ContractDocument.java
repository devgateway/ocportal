package org.devgateway.toolkit.persistence.dao.form;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.devgateway.toolkit.persistence.dao.AbstractChildAuditableEntity;
import org.devgateway.toolkit.persistence.dao.FileMetadata;
import org.devgateway.toolkit.persistence.dao.ListViewItem;
import org.devgateway.toolkit.persistence.dao.categories.ContractDocumentType;
import org.devgateway.toolkit.persistence.excel.annotation.ExcelExport;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Set;

/**
 * @author gmutuhu
 */
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Audited
@Table(indexes = {@Index(columnList = "parent_id")})
public class ContractDocument extends AbstractChildAuditableEntity<Contract> implements ListViewItem,
        SingleFileMetadatable {
    @ExcelExport(justExport = true, useTranslation = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToOne
    private ContractDocumentType contractDocumentType;

    @ExcelExport(justExport = true, useTranslation = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FileMetadata> formDocs;

    @Transient
    @JsonIgnore
    private Boolean expanded = false;

    public ContractDocumentType getContractDocumentType() {
        return contractDocumentType;
    }

    public void setContractDocumentType(final ContractDocumentType contractDocumentType) {
        this.contractDocumentType = contractDocumentType;
    }

    @Override
    public Set<FileMetadata> getFormDocs() {
        return formDocs;
    }

    public void setFormDocs(final Set<FileMetadata> formDocs) {
        this.formDocs = formDocs;
    }

    @Override
    @JsonIgnore
    public Boolean getEditable() {
        return null;
    }

    @Override
    public void setEditable(final Boolean editable) {

    }

    @Override
    @JsonIgnore
    public Boolean getExpanded() {
        return expanded;
    }

    @Override
    public void setExpanded(final Boolean expanded) {
        this.expanded = expanded;
    }
}
