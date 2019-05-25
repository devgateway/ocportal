package org.devgateway.toolkit.persistence.dao.categories;

import org.hibernate.envers.Audited;

import javax.persistence.Entity;

/**
 * 
 * @author gmutuhu
 *
 */
@Entity
@Audited
public class Item extends Category {
   
    @Override
    public String toString() {
        return getCode() + " - " + getLabel();
    }
}
