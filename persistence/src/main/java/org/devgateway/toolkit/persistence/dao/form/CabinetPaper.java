package org.devgateway.toolkit.persistence.dao.form;

import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.FileMetadata;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

/**
 * @author gmutuhu
 */

@Entity
@Audited
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(indexes = {@Index(columnList = "procurement_plan_id"),
        @Index(columnList = "number"),
        @Index(columnList = "name")})
public class CabinetPaper extends AbstractMakueniForm {
    @Column(length = DBConstants.STD_DEFAULT_TEXT_LENGTH)
    private String number;

    @Column(length = DBConstants.STD_DEFAULT_TEXT_LENGTH)
    private String name;

    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<FileMetadata> cabinetPaperDocs;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Set<FileMetadata> getCabinetPaperDocs() {
        return cabinetPaperDocs;
    }

    public void setCabinetPaperDocs(final Set<FileMetadata> cabinetPaperDocs) {
        this.cabinetPaperDocs = cabinetPaperDocs;
    }

    public final String getNumber() {
        return number;
    }

    public final void setNumber(final String number) {
        this.number = number;
    }

    @Override
    public void setLabel(final String label) {

    }

    @Override
    public String getLabel() {
        return name;
    }

    @Override
    public String toString() {
        return getLabel();
    }
}
