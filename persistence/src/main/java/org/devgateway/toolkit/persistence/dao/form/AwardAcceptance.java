package org.devgateway.toolkit.persistence.dao.form;

import org.devgateway.toolkit.persistence.dao.categories.Supplier;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author gmutuhu
 */
@Entity
@Audited
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(indexes = {@Index(columnList = "purchase_requisition_id")})
public class AwardAcceptance extends AbstractPurchaseReqMakueniEntity {

    private Date acceptanceDate;

    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToOne
    private Supplier awardee;

    private Double tenderValue;

    public Date getAcceptanceDate() {
        return acceptanceDate;
    }

    public void setAcceptanceDate(final Date acceptanceDate) {
        this.acceptanceDate = acceptanceDate;
    }

    public Supplier getAwardee() {
        return awardee;
    }

    public void setAwardee(final Supplier awardee) {
        this.awardee = awardee;
    }

    public Double getTenderValue() {
        return tenderValue;
    }

    public void setTenderValue(final Double tenderValue) {
        this.tenderValue = tenderValue;
    }

    @Override
    public void setLabel(final String label) {
    }

    @Override
    public String getLabel() {
        return null;
    }

}
