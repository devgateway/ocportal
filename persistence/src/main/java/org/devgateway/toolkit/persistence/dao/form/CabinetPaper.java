package org.devgateway.toolkit.persistence.dao.form;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author gmutuhu
 */

@Entity
@Audited
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(indexes = {@Index(columnList = "procurement_plan_id"),
        @Index(columnList = "number"),
        @Index(columnList = "name")})
public class CabinetPaper extends AbstractMakueniEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinColumn(name = "procurement_plan_id")
    @JsonIgnore
    private ProcurementPlan procurementPlan;

    @Column(length = DBConstants.STD_DEFAULT_TEXT_LENGTH)
    private String number;

    @Column(length = DBConstants.STD_DEFAULT_TEXT_LENGTH)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(final String number) {
        this.number = number;
    }

    @Override
    public void setLabel(final String label) {

    }

    public ProcurementPlan getProcurementPlan() {
        return procurementPlan;
    }

    public void setProcurementPlan(ProcurementPlan procurementPlan) {
        this.procurementPlan = procurementPlan;
    }

    @Override
    public String getLabel() {
        return name;
    }

    @Override
    public String toString() {
        return getLabel();
    }
}
