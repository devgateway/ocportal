package org.devgateway.toolkit.persistence.dao.form;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author gmutuhu
 */
@Entity
@Audited
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(indexes = {@Index(columnList = "purchase_requisition_id")})
public class TenderQuotationEvaluation extends AbstractPurchaseReqMakueniEntity {

    private Date closingDate;

    private Integer numberOfBids;

    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "parent_id")
    @OrderColumn(name = "index")
    private List<Bid> bids = new ArrayList<>();

    public Date getClosingDate() {
        return closingDate;
    }

    public Integer getNumberOfBids() {
        return numberOfBids;
    }

    public void setClosingDate(final Date closingDate) {
        this.closingDate = closingDate;
    }

    public void setNumberOfBids(final Integer numberOfBids) {
        this.numberOfBids = numberOfBids;
    }


    public List<Bid> getBids() {
        return bids;
    }

    public void setBids(final List<Bid> bids) {
        this.bids = bids;
    }

    @Override
    public void setLabel(final String label) {
    }

    @Override
    public String getLabel() {
        return null;
    }
}
