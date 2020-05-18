package org.devgateway.toolkit.persistence.dao.form;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.devgateway.toolkit.persistence.dao.AbstractDocsChildExpAuditEntity;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.ListViewItem;
import org.devgateway.toolkit.persistence.dao.categories.Supplier;
import org.devgateway.toolkit.persistence.dao.categories.SupplierResponse;
import org.devgateway.toolkit.persistence.excel.annotation.ExcelExport;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author mpostelnicu
 */
@Entity
@Audited
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AwardAcceptanceItem extends AbstractDocsChildExpAuditEntity<AwardAcceptance>
        implements ListViewItem {

    @ExcelExport(useTranslation = true, name = "Date")
    private Date acceptanceDate;

    @ExcelExport(name = "Supplier")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToOne
    private Supplier awardee;

    @ExcelExport(name = "Supplier Response")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToOne
    private SupplierResponse supplierResponse;

    @ExcelExport(useTranslation = true, name = "Accepted Award Value")
    private BigDecimal acceptedAwardValue;

    public Date getAcceptanceDate() {
        return acceptanceDate;
    }

    public void setAcceptanceDate(Date acceptanceDate) {
        this.acceptanceDate = acceptanceDate;
    }

    public Supplier getAwardee() {
        return awardee;
    }

    public void setAwardee(Supplier awardee) {
        this.awardee = awardee;
    }

    public BigDecimal getAcceptedAwardValue() {
        return acceptedAwardValue;
    }

    @JsonIgnore
    public AwardNotificationItem getExportableNotificationItem() {
        return Optional.ofNullable(getParent().getTenderProcess().getSingleAwardNotification())
                .filter(Statusable::isExportable).map(Stream::of).orElseGet(Stream::empty)
                .flatMap(a -> a.getItems().stream()).filter(a -> a.getAwardee().equals(awardee))
                .findFirst().orElse(null);
    }

    public boolean isAccepted() {
        return supplierResponse != null && supplierResponse.getLabel().equals(DBConstants.SupplierResponse.ACCEPTED);
    }

    public void setAcceptedAwardValue(BigDecimal acceptedAwardValue) {
        this.acceptedAwardValue = acceptedAwardValue;
    }

    public SupplierResponse getSupplierResponse() {
        return supplierResponse;
    }

    public void setSupplierResponse(SupplierResponse supplierResponse) {
        this.supplierResponse = supplierResponse;
    }

    @Override
    public String toString() {
        return Objects.toString(awardee, "") + " " + Objects.toString(acceptedAwardValue, "") + " "
                + Objects.toString(supplierResponse != null ? "(" + supplierResponse.getLabel() + ")" : "", "");

    }
}
