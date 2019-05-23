package org.devgateway.toolkit.persistence.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * @author gmutuhu
 */
public class StatusOverviewData implements Serializable {
    private static final long serialVersionUID = 3482410239645211004L;

    private ProcurementPlan procurementPlan;

    private List<StatusOverviewProjectStatus> projects = new ArrayList<>();

    @Transient
    @JsonIgnore
    private Boolean expanded = false;

    public ProcurementPlan getProcurementPlan() {
        return procurementPlan;
    }

    public void setProcurementPlan(final ProcurementPlan procurementPlan) {
        this.procurementPlan = procurementPlan;
    }

    public List<StatusOverviewProjectStatus> getProjects() {
        return projects;
    }

    public void setProjects(final List<StatusOverviewProjectStatus> projects) {
        this.projects = projects;
    }

    public Boolean getExpanded() {
        return expanded;
    }

    public StatusOverviewData setExpanded(final Boolean expanded) {
        this.expanded = expanded;
        return this;
    }
}
