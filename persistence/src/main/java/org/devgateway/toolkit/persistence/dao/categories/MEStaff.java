package org.devgateway.toolkit.persistence.dao.categories;

import org.hibernate.envers.Audited;

import javax.persistence.Entity;

/**
 * @author mpostelnicu
 */
@Entity
@Audited
public class MEStaff extends Category {

    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
