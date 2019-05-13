package org.devgateway.toolkit.persistence.dto;

import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * @author gmutuhu
 */
public class DepartmentOverviewData implements Serializable {
    private static final long serialVersionUID = 3482410239645211004L;

    private ProcurementPlan procurementPlan;

    private List<ProjectStatus> projects = new ArrayList<>();

    private Boolean expanded = false;

    public ProcurementPlan getProcurementPlan() {
        return procurementPlan;
    }

    public void setProcurementPlan(final ProcurementPlan procurementPlan) {
        this.procurementPlan = procurementPlan;
    }

    public List<ProjectStatus> getProjects() {
        return projects;
    }

    public void setProjects(final List<ProjectStatus> projects) {
        this.projects = projects;
    }

    public Boolean getExpanded() {
        return expanded;
    }

    public void setExpanded(final Boolean expanded) {
        this.expanded = expanded;
    }
}
