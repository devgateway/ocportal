package org.devgateway.toolkit.persistence.dao.form;

import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
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
public class CabinetPaper extends AbstractMakueniForm {
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

    public final String getNumber() {
        return number;
    }

    public final void setNumber(final String number) {
        this.number = number;
    }

    @Override
    public void setLabel(final String label) {

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
