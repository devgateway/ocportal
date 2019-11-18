package org.devgateway.toolkit.persistence.dao.form;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.devgateway.toolkit.persistence.dao.AbstractDocsChildExpAuditEntity;
import org.devgateway.toolkit.persistence.dao.ListViewItem;
import org.devgateway.toolkit.persistence.dao.categories.Supplier;
import org.devgateway.toolkit.persistence.excel.annotation.ExcelExport;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 * @author mpostelnicu
 */
@Entity
@Audited
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AwardNotificationItem extends AbstractDocsChildExpAuditEntity<AwardNotification>
        implements ListViewItem {

    @ExcelExport(useTranslation = true, name = "Date")
    private Date awardDate;

    @ExcelExport(useTranslation = true, name = "Award Value")
    private BigDecimal awardValue;

    @ExcelExport(name = "Supplier")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToOne
    private Supplier awardee;

    @ExcelExport(useTranslation = true, name = "Acknowledge Receipt of Award Timeline")
    private Integer acknowledgementDays;

    public Date getAwardDate() {
        return awardDate;
    }

    public void setAwardDate(Date awardDate) {
        this.awardDate = awardDate;
    }

    public BigDecimal getAwardValue() {
        return awardValue;
    }

    public void setAwardValue(BigDecimal awardValue) {
        this.awardValue = awardValue;
    }

    public Supplier getAwardee() {
        return awardee;
    }

    public void setAwardee(Supplier awardee) {
        this.awardee = awardee;
    }

    public Integer getAcknowledgementDays() {
        return acknowledgementDays;
    }

    public void setAcknowledgementDays(Integer acknowledgementDays) {
        this.acknowledgementDays = acknowledgementDays;
    }

    @Override
    public String toString() {
        return Objects.toString(awardee, "") + " " + Objects.toString(awardValue, "");
    }
}
