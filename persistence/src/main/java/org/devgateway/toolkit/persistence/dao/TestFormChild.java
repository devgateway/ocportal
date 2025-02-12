package org.devgateway.toolkit.persistence.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

/**
 * @author idobre
 * @since 2019-03-22
 */
@Entity
@Audited
@Table(indexes = {@Index(columnList = "test_form_id")})
public class TestFormChild extends AbstractAuditableEntity implements ListViewItem {
    @ManyToOne
    private TestForm testForm;

    private String header;

    private Integer value;

    @Transient
    @JsonIgnore
    private Boolean expanded = false;

    public TestForm getTestForm() {
        return testForm;
    }

    public void setTestForm(final TestForm testForm) {
        this.testForm = testForm;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(final String header) {
        this.header = header;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(final Integer value) {
        this.value = value;
    }

    @Override
    public AbstractAuditableEntity getParent() {
        return testForm;
    }

    @Override
    public Boolean getEditable() {
        return null;
    }

    @Override
    public void setEditable(final Boolean editable) {

    }

    @Override
    public Boolean getExpanded() {
        return expanded;
    }

    @Override
    public void setExpanded(final Boolean expanded) {
        this.expanded = expanded;
    }
}
