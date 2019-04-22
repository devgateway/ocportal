package org.devgateway.toolkit.persistence.dao.form;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.devgateway.toolkit.persistence.dao.AbstractAuditableEntity;
import org.devgateway.toolkit.persistence.dao.AbstractStatusAuditableEntity;
import org.devgateway.toolkit.persistence.dao.FileMetadata;
import org.devgateway.toolkit.persistence.dao.Labelable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.Set;

/**
 * @author idobre
 * @since 2019-04-01
 */
@MappedSuperclass
public abstract class AbstractMakueniForm extends AbstractStatusAuditableEntity implements Labelable {

    @ManyToOne(fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinColumn(name = "procurement_plan_id")
    @JsonIgnore
    private ProcurementPlan procurementPlan;

    private Date approvedDate;

    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<FileMetadata> formDocs;

    public ProcurementPlan getProcurementPlan() {
        return procurementPlan;
    }

    public void setProcurementPlan(final ProcurementPlan procurementPlan) {
        this.procurementPlan = procurementPlan;
    }

    @Override
    public AbstractAuditableEntity getParent() {
        return null;
    }

    public Date getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(final Date approvedDate) {
        this.approvedDate = approvedDate;
    }

    public Set<FileMetadata> getFormDocs() {
        return formDocs;
    }

    public void setFormDocs(final Set<FileMetadata> formDocs) {
        this.formDocs = formDocs;
    }
}
