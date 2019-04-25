package org.devgateway.toolkit.persistence.dao.categories;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.hibernate.envers.Audited;

/**
 * @author gmutuhu
 *
 */
@Entity
@Audited
public class ProcuringEntity extends Category {
    @Column(length = DBConstants.MAX_DEFAULT_TEXT_LENGTH_ONE_LINE)
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
