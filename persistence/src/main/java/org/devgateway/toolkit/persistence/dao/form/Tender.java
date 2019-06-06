package org.devgateway.toolkit.persistence.dao.form;

import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.categories.ProcurementMethod;
import org.devgateway.toolkit.persistence.dao.categories.ProcuringEntity;
import org.devgateway.toolkit.persistence.excel.annotation.ExcelExport;
import org.devgateway.toolkit.persistence.spring.PersistenceUtil;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author gmutuhu
 */
@Entity
@Audited
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(indexes = {@Index(columnList = "purchase_requisition_id"),
        @Index(columnList = "tenderTitle"),
        @Index(columnList = "tenderNumber")})
public class Tender extends AbstractPurchaseReqMakueniEntity {
    @ExcelExport(useTranslation = true)
    @Column(length = DBConstants.STD_DEFAULT_TEXT_LENGTH)
    private String tenderNumber;

    @ExcelExport(useTranslation = true)
    @Column(length = DBConstants.STD_DEFAULT_TEXT_LENGTH)
    private String tenderTitle;

    @ExcelExport(useTranslation = true)
    private Date invitationDate;

    @ExcelExport(useTranslation = true)
    private Date closingDate;

    @ExcelExport(justExport = true, useTranslation = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToOne
    private ProcurementMethod procurementMethod;

    @ExcelExport(useTranslation = true)
    @Column(length = DBConstants.MAX_DEFAULT_TEXT_AREA)
    private String objective;

    @ExcelExport(justExport = true, useTranslation = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToOne
    private ProcuringEntity issuedBy;

    @ExcelExport(useTranslation = true)
    private Double tenderValue;

    @ExcelExport(useTranslation = true)
    @Column(length = DBConstants.MAX_DEFAULT_TEXT_LENGTH_ONE_LINE)
    private String tenderLink;

    @ExcelExport(separateSheet = true, useTranslation = true)
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
        return tenderNumber + " " + tenderTitle;
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

    @Override
    public String toString() {
        return getLabel();
    }

    public Double getTotalAmount() {
        Double total = 0d;
        for (TenderItem item : tenderItems) {
            if (item.getUnitPrice() != null && item.getQuantity() != null) {
                total += item.getUnitPrice() * item.getQuantity();
            }
        }
        return total;
    }

    @Override
    @Transactional
    public Collection<AbstractMakueniEntity> getDirectChildrenEntities() {
        return Collections.singletonList(PersistenceUtil.getNext(getPurchaseRequisitionNotNull()
                .getTenderQuotationEvaluation()));
    }

}
