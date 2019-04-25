package org.devgateway.toolkit.persistence.dao.categories;

import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author gmutuhu
 */
@Entity
@Audited
public class Supplier extends Category {
    
    @Column(length = DBConstants.MAX_DEFAULT_TEXT_LENGTH_ONE_LINE)
    private String address;

    public final String getAddress() {
        return address;
    }

    public final void setAddress(final String address) {
        this.address = address;
    }
}
