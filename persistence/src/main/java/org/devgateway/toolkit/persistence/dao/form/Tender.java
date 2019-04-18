package org.devgateway.toolkit.persistence.dao.form;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.devgateway.toolkit.persistence.dao.FileMetadata;
import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.categories.ProcurementMethod;
import org.devgateway.toolkit.persistence.dao.categories.ProcuringEntity;
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
@Table(indexes = {@Index(columnList = "procurement_plan_id"), @Index(columnList = "tenderTitle")})
public class Tender extends AbstractMakueniForm {
    private String tenderNumber;
    private String tenderTitle;
    private Date invitationDate;
    private Date closingDate;
    
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToOne
    private ProcurementMethod procurementMethod;
    private String objective;
    
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToOne
    private ProcuringEntity issuedBy;
    private Double tenderValue;
    
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<FileMetadata> tenderDocs;
    private String tenderLink;
        
    
    @Override
    public void setLabel(final String label) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getLabel() {
        // TODO Auto-generated method stub
        return null;
    }

    public final String getTenderNumber() {
        return tenderNumber;
    }

    public final void setTenderNumber(final String tenderNumber) {
        this.tenderNumber = tenderNumber;
    }

    public final String getTenderTitle() {
        return tenderTitle;
    }

    public final void setTenderTitle(final String tenderTitle) {
        this.tenderTitle = tenderTitle;
    }

    public final Date getInvitationDate() {
        return invitationDate;
    }

    public final void setInvitationDate(final Date invitationDate) {
        this.invitationDate = invitationDate;
    }

    public final Date getClosingDate() {
        return closingDate;
    }

    public final void setClosingDate(final Date closingDate) {
        this.closingDate = closingDate;
    }

    public final ProcurementMethod getProcurementMethod() {
        return procurementMethod;
    }

    public final void setProcurementMethod(final ProcurementMethod procurementMethod) {
        this.procurementMethod = procurementMethod;
    }

    public final String getObjective() {
        return objective;
    }

    public final void setObjective(final String objective) {
        this.objective = objective;
    }

    public final ProcuringEntity getIssuedBy() {
        return issuedBy;
    }

    public final void setIssuedBy(final ProcuringEntity issuedBy) {
        this.issuedBy = issuedBy;
    }

    public final Double getTenderValue() {
        return tenderValue;
    }

    public final void setTenderValue(final Double tenderValue) {
        this.tenderValue = tenderValue;
    }

    public final Set<FileMetadata> getTenderDocs() {
        return tenderDocs;
    }

    public final void setTenderDocs(final Set<FileMetadata> tenderDocs) {
        this.tenderDocs = tenderDocs;
    }

    public final String getTenderLink() {
        return tenderLink;
    }

    public final void setTenderLink(final String tenderLink) {
        this.tenderLink = tenderLink;
    }

}
