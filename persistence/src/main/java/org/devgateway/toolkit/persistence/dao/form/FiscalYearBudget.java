package org.devgateway.toolkit.persistence.dao.form;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.devgateway.toolkit.persistence.dao.AbstractAuditableEntity;
import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.excel.annotation.ExcelExport;
import org.devgateway.toolkit.persistence.validator.Severity;
import org.devgateway.toolkit.persistence.validator.groups.HighLevel;
import org.devgateway.toolkit.persistence.validator.validators.OneBudgetPerDepartmentAndFY;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;

/**
 * @author mpostelnicu
 */
@Entity
@Audited
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"department_id", "fiscal_year_id"}))
@OneBudgetPerDepartmentAndFY(groups = HighLevel.class, payload = Severity.NonRecoverable.class)
public class FiscalYearBudget extends AbstractClientEntity {

    @ExcelExport(justExport = true, useTranslation = true, name = "Fiscal Year")
    @ManyToOne(optional = false)
    private FiscalYear fiscalYear;

    @ExcelExport(justExport = true, useTranslation = true, name = "Department")
    @ManyToOne(optional = false)
    private Department department;

    @ExcelExport(useTranslation = true, name = "Amount Budgeted")
    @NotNull
    private BigDecimal amountBudgeted;

    public FiscalYear getFiscalYear() {
        return fiscalYear;
    }

    public void setFiscalYear(FiscalYear fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    @Override
    @Transactional
    @JsonIgnore
    @org.springframework.data.annotation.Transient
    public Collection<? extends AbstractClientEntity> getDirectChildrenEntities() {
        return Collections.emptyList();
    }

    @Override
    public AbstractAuditableEntity getParent() {
        return null;
    }

    public BigDecimal getAmountBudgeted() {
        return amountBudgeted;
    }

    public void setAmountBudgeted(BigDecimal amountBudgeted) {
        this.amountBudgeted = amountBudgeted;
    }


    @Override
    public void setLabel(String label) {

    }

    @Override
    public String getLabel() {
        return fiscalYear + "-" + department;
    }
}
