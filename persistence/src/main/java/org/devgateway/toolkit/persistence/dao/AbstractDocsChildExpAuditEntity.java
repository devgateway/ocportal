package org.devgateway.toolkit.persistence.dao;

import org.devgateway.toolkit.persistence.excel.annotation.ExcelExport;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import java.util.Set;

/**
 * @author mpostelnicu
 */
public abstract class AbstractDocsChildExpAuditEntity<P extends AbstractAuditableEntity> extends
        AbstractChildExpandableAuditEntity<P> {

    @ExcelExport(justExport = true, useTranslation = true, name = "Documents")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FileMetadata> formDocs;

    public Set<FileMetadata> getFormDocs() {
        return formDocs;
    }

    public void setFormDocs(Set<FileMetadata> formDocs) {
        this.formDocs = formDocs;
    }
}
