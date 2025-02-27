package org.devgateway.toolkit.persistence.dao.categories;

import org.hibernate.envers.Audited;

import jakarta.persistence.Entity;

/**
 * @author mpostelnicu
 */
@Entity
@Audited
public class PMCStaff extends Category {

    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return getLabel() + " (tel " + phone + ")";
    }
}
