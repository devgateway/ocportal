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
public class AwardAcceptance extends AbstractTenderProcessMakueniEntity {
    @ExcelExport(useTranslation = true, name = "Date")
    private Date acceptanceDate;

    @ExcelExport(name = "Supplier")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToOne
    private Supplier awardee;

    @ExcelExport(useTranslation = true, name = "Accepted Award Value")
    private BigDecimal acceptedAwardValue;

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

    public BigDecimal getAcceptedAwardValue() {
        return acceptedAwardValue;
    }

    public void setAcceptedAwardValue(final BigDecimal acceptedAwardValue) {
        this.acceptedAwardValue = acceptedAwardValue;
    }

    @Override
    public void setLabel(final String label) {
    }

    @Override
    @JsonIgnore
    @org.springframework.data.annotation.Transient
    public String getLabel() {
        return "Award acceptance for tender process " + getTenderProcessNotNull().getLabel();
    }

    @Override
    @Transactional
    public Collection<? extends AbstractMakueniEntity> getDirectChildrenEntities() {
        return Collections.singletonList(PersistenceUtil.getNext(getTenderProcessNotNull().getContract()));
    }
}
