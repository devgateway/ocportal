package org.devgateway.toolkit.persistence.dao.form;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * @author idobre
 * @since 2019-04-17
 */
@Entity
@Audited
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(indexes = {@Index(columnList = "procurement_plan_id")})
public class PurchaseRequisition extends AbstractMakueniForm {


    @Override
    public void setLabel(final String label) {

    }

    @Override
    public String getLabel() {
        return null;
    }
}
