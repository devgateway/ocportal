package org.devgateway.toolkit.persistence.dao.form;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.FileMetadata;
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
@Table(indexes = {@Index(columnList = "procurement_plan_id"), 
        @Index(columnList = "tenderTitle"), 
        @Index(columnList = "tenderNumber")})
public class Tender extends AbstractMakueniForm {
    @Column(length = DBConstants.STD_DEFAULT_TEXT_LENGTH)
    private String tenderNumber;    
    @Column(length = DBConstants.STD_DEFAULT_TEXT_LENGTH)
    private String tenderTitle;
    private Date invitationDate;
    private Date closingDate;
    
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToOne
    private ProcurementMethod procurementMethod;
    
    @Column(length = DBConstants.MAX_DEFAULT_TEXT_AREA)
    private String objective;
    
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToOne
    private ProcuringEntity issuedBy;
    private Double tenderValue;
    
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<FileMetadata> tenderDocs;
    
    @Column(length = DBConstants.MAX_DEFAULT_TEXT_LENGTH_ONE_LINE)
    private String tenderLink;
        
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "parent_id")
    @OrderColumn(name = "index")
    private List<TenderItem> tenderItems = new ArrayList<>();
    
    @Override
    public void setLabel(final String label) {
        
    }

    @Override
    public String getLabel() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getTenderNumber() {
        return tenderNumber;
    }

    public void setTenderNumber(final String tenderNumber) {
        this.tenderNumber = tenderNumber;
    }

    public String getTenderTitle() {
        return tenderTitle;
    }

    public void setTenderTitle(final String tenderTitle) {
        this.tenderTitle = tenderTitle;
    }

    public Date getInvitationDate() {
        return invitationDate;
    }

    public void setInvitationDate(final Date invitationDate) {
        this.invitationDate = invitationDate;
    }

    public Date getClosingDate() {
        return closingDate;
    }

    public void setClosingDate(final Date closingDate) {
        this.closingDate = closingDate;
    }

    public ProcurementMethod getProcurementMethod() {
        return procurementMethod;
    }

    public void setProcurementMethod(final ProcurementMethod procurementMethod) {
        this.procurementMethod = procurementMethod;
    }

    public String getObjective() {
        return objective;
    }

    public void setObjective(final String objective) {
        this.objective = objective;
    }

    public ProcuringEntity getIssuedBy() {
        return issuedBy;
    }
   
    public void setIssuedBy(final ProcuringEntity issuedBy) {
        this.issuedBy = issuedBy;
    }

    public Double getTenderValue() {
        return tenderValue;
    }

    public void setTenderValue(final Double tenderValue) {
        this.tenderValue = tenderValue;
    }

    public Set<FileMetadata> getTenderDocs() {
        return tenderDocs;
    }

    public void setTenderDocs(final Set<FileMetadata> tenderDocs) {
        this.tenderDocs = tenderDocs;
    }

    public String getTenderLink() {
        return tenderLink;
    }

    public void setTenderLink(final String tenderLink) {
        this.tenderLink = tenderLink;
    }

    public List<TenderItem> getTenderItems() {
        return tenderItems;
    }

    public void setTenderItems(final List<TenderItem> tenderItems) {
        this.tenderItems = tenderItems;
    }

}
