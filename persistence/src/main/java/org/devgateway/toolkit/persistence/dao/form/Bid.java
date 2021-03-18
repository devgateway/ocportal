package org.devgateway.toolkit.persistence.dao.form;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.devgateway.toolkit.persistence.dao.AbstractChildExpandableAuditEntity;
import org.devgateway.toolkit.persistence.dao.ListViewItem;
import org.devgateway.toolkit.persistence.dao.categories.Supplier;
import org.devgateway.toolkit.persistence.excel.annotation.ExcelExport;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author gmutuhu
 */

@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Audited
@Table(indexes = {@Index(columnList = "parent_id")})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Bid extends AbstractChildExpandableAuditEntity<TenderQuotationEvaluation> implements ListViewItem {
    @ExcelExport(name = "Supplier")
    @ManyToOne
    private Supplier supplier;

    @ExcelExport(useTranslation = true, name = "Supplier Responsiveness")
    private String supplierResponsiveness;

    @ExcelExport(useTranslation = true, name = "Quoted Price")
    private BigDecimal quotedAmount;

    @Transient
    private List<String> prequalifiedItems;

    public Supplier getSupplier() {
        return supplier;
    }

    public String getSupplierResponsiveness() {
        return supplierResponsiveness;
    }

    public BigDecimal getQuotedAmount() {
        return quotedAmount;
    }

    public void setQuotedAmount(final BigDecimal quotedAmount) {
        this.quotedAmount = quotedAmount;
    }

    public void setSupplier(final Supplier supplier) {
        this.supplier = supplier;
    }

    public void setSupplierResponsiveness(final String supplierResponsiveness) {
        this.supplierResponsiveness = supplierResponsiveness;
    }

    public List<String> getPrequalifiedItems() {
        return prequalifiedItems;
    }

    public void setPrequalifiedItems(List<String> prequalifiedItems) {
        this.prequalifiedItems = prequalifiedItems;
    }
}
