package org.devgateway.toolkit.persistence.dao.form;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.devgateway.toolkit.persistence.dao.form.abstracted.ProcurementEditable;
import org.devgateway.toolkit.persistence.excel.annotation.ExcelExport;
import org.devgateway.toolkit.persistence.spring.PersistenceUtil;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author idobre
 * @since 2019-04-24
 */
@Entity
@Audited
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(indexes = {@Index(columnList = "tender_process_id")})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfessionalOpinion extends AbstractTenderProcessMakueniEntity implements ProcurementEditable {

    @ExcelExport(name = "Professional Opinions", separateSheet = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "parent_id")
    @OrderColumn(name = "index")
    private List<ProfessionalOpinionItem> items = new ArrayList<>();

    @Override
    public void setLabel(final String label) {

    }

    @Override
    @JsonIgnore
    @org.springframework.data.annotation.Transient
    public String getLabel() {
        return "Professional Opinions for tender process " + getTenderProcessNotNull().getLabel();
    }

    @Override
    public String toString() {
        return getLabel();
    }

    @Override
    @Transactional
    @JsonIgnore
    @org.springframework.data.annotation.Transient
    public Collection<? extends AbstractMakueniEntity> getDirectChildrenEntities() {
        return Collections.singletonList(PersistenceUtil.getNext(getTenderProcessNotNull()
                .getAwardNotification()));
    }

    public List<ProfessionalOpinionItem> getItems() {
        return items;
    }

    public void setItems(List<ProfessionalOpinionItem> items) {
        this.items = items;
    }
}
