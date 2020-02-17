package org.devgateway.toolkit.persistence.dao.form;

import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@MappedSuperclass
public abstract class AbstractImplTenderProcessMakueniEntity extends AbstractTenderProcessMakueniEntity {

    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToOne
    private Contract contract;

    public Contract getContract() {
        return contract;
    }

    @Override
    public String getLabel() {
        return getClass().getSimpleName() + " " + getId() + (
                getApprovedDate() == null ? "" : (" with date "
                        + getApprovedDate().toInstant().atZone(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern(DBConstants.DATE_FORMAT))));
    }

    @Override
    public String toString() {
        return getLabel();
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }
}
