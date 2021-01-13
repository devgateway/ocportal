package org.devgateway.toolkit.persistence.dao.form;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.devgateway.toolkit.persistence.dao.AbstractAuditableEntity;
import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.util.Objects;

@MappedSuperclass
public abstract class AbstractTenderProcessMakueniEntity extends AbstractMakueniEntity implements ProjectAttachable,
        ProcurementPlanAttachable {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tender_process_id")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    protected TenderProcess tenderProcess;

    @Override
    @JsonIgnore
    @org.springframework.data.annotation.Transient
    public AbstractAuditableEntity getParent() {
        return tenderProcess;
    }

    @JsonIgnore
    @org.springframework.data.annotation.Transient
    public TenderProcess getTenderProcess() {
        return tenderProcess;
    }

    public void setTenderProcess(final TenderProcess tenderProcess) {
        this.tenderProcess = tenderProcess;
    }

    @Override
    @JsonIgnore
    @org.springframework.data.annotation.Transient
    public Project getProject() {
        return getTenderProcess().getProject();
    }

    @JsonIgnore
    @org.springframework.data.annotation.Transient
    public TenderProcess getTenderProcessNotNull() {
        Objects.requireNonNull(tenderProcess, "Tender process must not be null at this stage!");
        return tenderProcess;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "-" + getId();
    }

    @Override
    @JsonIgnore
    @org.springframework.data.annotation.Transient
    public ProcurementPlan getProcurementPlan() {
        if (tenderProcess != null) {
            return tenderProcess.getProcurementPlan();
        }
        return null;
    }

    @Override
    @JsonIgnore
    @Transactional
    @org.springframework.data.annotation.Transient
    public Department getDepartment() {
        return getTenderProcessNotNull().getDepartment();
    }

    /**
     * Return the next form to fill out once the current one was completed.
     */
    @JsonIgnore
    public abstract Class<?> getNextForm();

    /**
     * Return true to prevent deletion of the current form if there are downstream forms that depend on this one.
     */
    @JsonIgnore
    public abstract boolean hasDownstreamForms();
}
