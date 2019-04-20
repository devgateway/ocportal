package org.devgateway.toolkit.persistence.dao.categories;

import javax.persistence.Entity;

import org.hibernate.envers.Audited;

/**
 * @author gmutuhu
 *
 */
@Entity
@Audited
public class ProcuringEntity extends Category {
    private String address;
    private String emailAddress;
   
    public final String getAddress() {
        return address;
    }
    public final void setAddress(final String address) {
        this.address = address;
    }
    public final String getEmailAddress() {
        return emailAddress;
    }
    public final void setEmailAddress(final String emailAddress) {
        this.emailAddress = emailAddress;
    }

}
