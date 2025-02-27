package org.devgateway.toolkit.persistence.dao.form;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.FileMetadata;
import org.devgateway.toolkit.persistence.dao.Form;
import org.devgateway.toolkit.persistence.excel.annotation.ExcelExport;
import org.devgateway.toolkit.persistence.spring.PersistenceUtil;
import org.devgateway.toolkit.persistence.validator.Severity;
import org.devgateway.toolkit.persistence.validator.groups.HighLevel;
import org.devgateway.toolkit.persistence.validator.validators.MaxAttachedFiles;
import org.devgateway.toolkit.persistence.validator.validators.UniqueTenderProcessEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author mpostelnicu
 * @since 2019-04-24
 */
@Entity
@Audited
@Table(indexes = {@Index(columnList = "tender_process_id")},
        uniqueConstraints =
        @UniqueConstraint(columnNames = "tender_process_id"))
@JsonInclude(JsonInclude.Include.NON_NULL)
@Form(featureName = "purchaseRequisitionForm")
@UniqueTenderProcessEntity(groups = HighLevel.class, payload = Severity.NonRecoverable.class,
        message = "{org.devgateway.toolkit.persistence.dao.form.UniquePurchaseRequisitionGroup.message}")
public class PurchaseRequisitionGroup extends AbstractTenderProcessClientEntity {

    @ExcelExport(name = "Purchase Requisitions", separateSheet = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "parent_id")
    @OrderColumn(name = "index")
    private List<PurchRequisition> purchRequisitions = new ArrayList<>();

    @ExcelExport(useTranslation = true, name = "Purchase Request Number")
    @Column(length = DBConstants.STD_DEFAULT_TEXT_LENGTH)
    private String purchaseRequestNumber;


    @Override
    public void setLabel(final String label) {

    }

    @Override
    @JsonIgnore
    @org.springframework.data.annotation.Transient
    public String getLabel() {
        return purchaseRequestNumber;
    }

    @Transient
    public List<PurchaseItem> getPurchaseItems() {
        return purchRequisitions.stream().flatMap(pr -> pr.getPurchaseItems().stream()).collect(Collectors.toList());
    }

    public List<PurchRequisition> getPurchRequisitions() {
        return purchRequisitions;
    }

    public void setPurchRequisitions(List<PurchRequisition> purchRequisitions) {
        this.purchRequisitions = purchRequisitions;
    }

    @JsonIgnore
    @org.springframework.data.annotation.Transient
    public BigDecimal getAmount() {
        BigDecimal amount = BigDecimal.ZERO;
        for (PurchRequisition pr : purchRequisitions) {
            for (PurchaseItem item : pr.getPurchaseItems()) {
                if (item.getAmount() != null && item.getQuantity() != null) {
                    amount = amount.add(item.getAmount().multiply(item.getQuantity()));
                }
            }
        }

        return amount;
    }

    @Override
    public String toString() {
        return getLabel();
    }

    @Override
    @Transactional
    @JsonIgnore
    @org.springframework.data.annotation.Transient
    protected Collection<? extends AbstractClientEntity> getDirectChildrenEntities() {
        return Collections.singletonList(PersistenceUtil.getNext(getTenderProcessNotNull()
                .getTender()));
    }

    public String getPurchaseRequestNumber() {
        return purchaseRequestNumber;
    }

    public void setPurchaseRequestNumber(String purchaseRequestNumber) {
        this.purchaseRequestNumber = purchaseRequestNumber;
    }

    @Override
    public Class<?> getNextForm() {
        return Tender.class;
    }

    @Override
    public boolean hasDownstreamForms() {
        return getTenderProcess().hasFormsDependingOnPurchaseRequisition();
    }

    @MaxAttachedFiles
    @JsonIgnore
    @org.springframework.data.annotation.Transient
    public Collection<FileMetadata> getAllAttachedFiles() {
        return purchRequisitions.stream()
                .flatMap(pr -> pr.getFormDocs().stream())
                .collect(Collectors.toCollection(HashSet::new));
    }
}
