package org.devgateway.toolkit.persistence.dao.categories;

import org.hibernate.envers.Audited;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

/**
 * 
 * @author gmutuhu
 *
 */
@Entity
@Audited
public class Staff extends Category {
    private String title;

    @ManyToOne
    private Department department;

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(final Department department) {
        this.department = department;
    }
}
