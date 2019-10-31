package org.devgateway.toolkit.persistence.dao.form;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.devgateway.toolkit.persistence.dao.categories.Supplier;
import org.devgateway.toolkit.persistence.excel.annotation.ExcelExport;
import org.devgateway.toolkit.persistence.spring.PersistenceUtil;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

/**
 * @author gmutuhu
 */
@Entity
@Audited
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(indexes = {@Index(columnList = "tender_process_id")})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AwardNotification extends AbstractTenderProcessMakueniEntity {
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

    public BigDecimal getAwardValue() {
        return awardValue;
    }

    public void setAwardValue(final BigDecimal awardValue) {
        this.awardValue = awardValue;
    }

    public Integer getAcknowledgementDays() {
        return acknowledgementDays;
    }

    public void setAcknowledgementDays(final Integer acknowledgementDays) {
        this.acknowledgementDays = acknowledgementDays;
    }

    public Date getAwardDate() {
        return awardDate;
    }

    public void setAwardDate(final Date awardDate) {
        this.awardDate = awardDate;
    }

    public Supplier getAwardee() {
        return awardee;
    }

    public void setAwardee(final Supplier awardee) {
        this.awardee = awardee;
    }

    @Override
    public void setLabel(final String label) {
    }

    @Override
    @JsonIgnore
    @org.springframework.data.annotation.Transient
    public String getLabel() {
        return "Award notification for tender process " + getTenderProcessNotNull().getLabel();
    }

    @Override
    @Transactional
    public Collection<? extends AbstractMakueniEntity> getDirectChildrenEntities() {
        return Collections.singletonList(PersistenceUtil.getNext(getTenderProcessNotNull().getAwardAcceptance()));
    }
}
