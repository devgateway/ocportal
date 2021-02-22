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
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

/**
 * @author mpostelnicu
 */
@Entity
@Audited
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(indexes = {@Index(columnList = "name")},
        uniqueConstraints = {@UniqueConstraint(columnNames = "name"),
                @UniqueConstraint(columnNames = {"startYear", "endYear"})})
@UniquePrequalificationYearRange(groups = HighLevel.class, payload = Severity.NonRecoverable.class,
        message = "{org.devgateway.toolkit.persistence.dao.prequalification.UniquePrequalificationYearRange.message}")
@NonOverlappingPrequalificationYearRange(groups = HighLevel.class, payload = Severity.NonRecoverable.class, message =
         "{org.devgateway.toolkit.persistence.dao.prequalification.NonOverlappingPrequalificationYearRange.message}")
@YearOrderPrequalificationYearRange(groups = HighLevel.class, payload = Severity.NonRecoverable.class, message =
        "{org.devgateway.toolkit.persistence.dao.prequalification.YearOrderPrequalificationYearRange.message}")
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
}