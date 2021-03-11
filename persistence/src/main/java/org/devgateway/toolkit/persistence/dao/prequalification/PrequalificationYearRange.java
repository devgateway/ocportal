/*******************************************************************************
 * Copyright (c) 2015 Development Gateway, Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License (MIT)
 * which accompanies this distribution, and is available at
 * https://opensource.org/licenses/MIT
 *
 * Contributors:
 * Development Gateway - initial API and implementation
 *******************************************************************************/
package org.devgateway.toolkit.persistence.dao.prequalification;

import org.devgateway.toolkit.persistence.dao.AbstractAuditableEntity;
import org.devgateway.toolkit.persistence.dao.Labelable;
import org.devgateway.toolkit.persistence.validator.Severity;
import org.devgateway.toolkit.persistence.validator.groups.HighLevel;
import org.devgateway.toolkit.persistence.validator.groups.NonDraft;
import org.devgateway.toolkit.persistence.validator.validators.NonOverlappingPrequalificationYearRange;
import org.devgateway.toolkit.persistence.validator.validators.UniquePrequalificationSchema;
import org.devgateway.toolkit.persistence.validator.validators.UniquePrequalificationYearRange;
import org.devgateway.toolkit.persistence.validator.validators.YearOrderPrequalificationYearRange;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Check;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 * @author mpostelnicu
 */
@Entity
@Audited
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(indexes = {@Index(columnList = "name")},
        uniqueConstraints = {@UniqueConstraint(columnNames = "name"),
                @UniqueConstraint(columnNames = {"startYear", "endYear"})})
@UniquePrequalificationYearRange(groups = HighLevel.class,
        message = "{org.devgateway.toolkit.persistence.dao.prequalification.UniquePrequalificationYearRange.message}")
@NonOverlappingPrequalificationYearRange(groups = HighLevel.class, message =
         "{org.devgateway.toolkit.persistence.dao.prequalification.NonOverlappingPrequalificationYearRange.message}")
@YearOrderPrequalificationYearRange(groups = HighLevel.class, message =
        "{org.devgateway.toolkit.persistence.dao.prequalification.YearOrderPrequalificationYearRange.message}")
@Check(constraints = "start_year <= end_year")
public class PrequalificationYearRange extends AbstractAuditableEntity implements Labelable {

    @NotNull
    @Column(nullable = false)
    private String name;

    @NotNull
    @Column(nullable = false)
    private Integer startYear;

    @NotNull
    @Column(nullable = false)
    private Integer endYear;

    @ManyToOne(optional = false)
    @NotNull
    private PrequalificationSchema schema;

    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(mappedBy = "yearRange")
    private Set<PrequalifiedSupplier> prequalifiedSuppliers = new HashSet<>();

    public PrequalificationYearRange() {
    }

    @Override
    public AbstractAuditableEntity getParent() {
        return null;
    }

    @Override
    public void setLabel(String label) {

    }

    @Override
    public String getLabel() {
        return name;
    }

    public Integer getStartYear() {
        return startYear;
    }

    public void setStartYear(Integer startYear) {
        this.startYear = startYear;
    }

    public Integer getEndYear() {
        return endYear;
    }

    public void setEndYear(Integer endYear) {
        this.endYear = endYear;
    }

    public PrequalificationSchema getSchema() {
        return schema;
    }

    public void setSchema(PrequalificationSchema schema) {
        this.schema = schema;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
       return getLabel();
    }

    public Set<PrequalifiedSupplier> getPrequalifiedSuppliers() {
        return prequalifiedSuppliers;
    }

    public void setPrequalifiedSuppliers(Set<PrequalifiedSupplier> prequalifiedSuppliers) {
        this.prequalifiedSuppliers = prequalifiedSuppliers;
    }
}