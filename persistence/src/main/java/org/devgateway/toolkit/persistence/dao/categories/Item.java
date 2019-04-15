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
    private String itemCode;
    
    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(final String itemCode) {
        this.itemCode = itemCode;
    }

    @Override
    public String toString() {
        return getItemCode() + " - " + getLabel();
    }
}
