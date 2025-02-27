package org.devgateway.toolkit.persistence.dao.alerts;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

import org.devgateway.toolkit.persistence.dao.AbstractAuditableEntity;
import org.devgateway.toolkit.persistence.dao.form.MEReport;
import org.devgateway.toolkit.persistence.dao.form.PMCReport;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

/**
 * @author Octavian Ciubotaru
 */
@Entity
@Audited
public class ApprovedReport extends AbstractAuditableEntity {

    @ManyToOne
    private MEReport meReport;

    @ManyToOne
    private PMCReport pmcReport;

    private Boolean processed = false;

    public MEReport getMeReport() {
        return meReport;
    }

    public void setMeReport(MEReport meReport) {
        this.meReport = meReport;
    }

    public PMCReport getPmcReport() {
        return pmcReport;
    }

    public void setPmcReport(PMCReport pmcReport) {
        this.pmcReport = pmcReport;
    }

    public Boolean getProcessed() {
        return processed;
    }

    public void setProcessed(Boolean processed) {
        this.processed = processed;
    }

    @Override
    public AbstractAuditableEntity getParent() {
        return null;
    }
}
