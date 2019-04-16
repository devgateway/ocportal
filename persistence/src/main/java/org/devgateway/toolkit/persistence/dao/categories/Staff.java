package org.devgateway.toolkit.persistence.dao.categories;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * 
 * @author gmutuhu
 *
 */
@Entity
@Audited
public class Staff extends Category {
    private String title;

    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToOne
    private Department department;

    public final String getTitle() {
        return title;
    }

    public final void setTitle(final String title) {
        this.title = title;
    }

    public final Department getDepartment() {
        return department;
    }

    public final void setDepartment(final Department department) {
        this.department = department;
    }

}
