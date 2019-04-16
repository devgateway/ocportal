package org.devgateway.toolkit.persistence.dao.categories;

import javax.persistence.Entity;

import org.hibernate.envers.Audited;

/**
 * @author gmutuhu
 *
 */
@Entity
@Audited
public class Supplier extends Category {
    private String address;
   
    public final String getAddress() {
        return address;
    }
    public final void setAddress(final String address) {
        this.address = address;
    }

}
