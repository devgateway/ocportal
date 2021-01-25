package org.devgateway.toolkit.persistence.dto;

import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * @author gmutuhu
 */
public class StatusOverviewRowGroup implements Serializable {
    private static final long serialVersionUID = 3482410239645211004L;

    private ProcurementPlan procurementPlan;

    private List<StatusOverviewRowInfo> rows = new ArrayList<>();

    public ProcurementPlan getProcurementPlan() {
        return procurementPlan;
    }

    public void setProcurementPlan(final ProcurementPlan procurementPlan) {
        this.procurementPlan = procurementPlan;
    }

    public List<StatusOverviewRowInfo> getRows() {
        return rows;
    }

    public void setRows(final List<StatusOverviewRowInfo> rows) {
        this.rows = rows;
    }
}
