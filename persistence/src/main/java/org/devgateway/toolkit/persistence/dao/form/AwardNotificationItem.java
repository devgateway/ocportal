package org.devgateway.toolkit.persistence.dao.form;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.devgateway.toolkit.persistence.dao.AbstractDocsChildExpAuditEntity;
import org.devgateway.toolkit.persistence.dao.FileMetadata;
import org.devgateway.toolkit.persistence.dao.ListViewItem;
import org.devgateway.toolkit.persistence.dao.categories.Supplier;
import org.devgateway.toolkit.persistence.excel.annotation.ExcelExport;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

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

    @ExcelExport(justExport = true, useTranslation = true, name = "Letters of Regret")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FileMetadata> lettersOfRegret = new HashSet<>();

    @JsonIgnore
    public AwardAcceptanceItem getExportableAcceptanceItem() {
        return Optional.ofNullable(getParent().getTenderProcess().getSingleAwardAcceptance())
                .filter(Statusable::isExportable).map(Stream::of).orElseGet(Stream::empty)
                .flatMap(a -> a.getItems().stream()).filter(a -> a.getAwardee().equals(awardee))
                .findFirst().orElse(null);

    }

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

    public Set<FileMetadata> getLettersOfRegret() {
        return lettersOfRegret;
    }

    public void setLettersOfRegret(Set<FileMetadata> lettersOfRegret) {
        this.lettersOfRegret = lettersOfRegret;
    }

    @Override
    public String toString() {
        return Objects.toString(awardee, "") + " " + Objects.toString(awardValue, "");
    }
}
