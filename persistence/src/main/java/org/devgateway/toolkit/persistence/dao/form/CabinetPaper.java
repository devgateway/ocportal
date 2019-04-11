package org.devgateway.toolkit.persistence.dao.form;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import org.devgateway.toolkit.persistence.dao.FileMetadata;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

/**
 * @author gmutuhu
 *
 */

@Entity
@Audited
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CabinetPaper extends AbstractMakueniForm {
    private String number;
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
}
