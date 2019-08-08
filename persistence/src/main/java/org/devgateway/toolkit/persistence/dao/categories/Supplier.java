package org.devgateway.toolkit.persistence.dao.categories;

import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.excel.annotation.ExcelExport;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * @author gmutuhu
 */
@Entity
@Audited
public class Supplier extends Category {
    @ExcelExport(name = "Address")
    @Column(length = DBConstants.MAX_DEFAULT_TEXT_LENGTH_ONE_LINE)
    private String address;

    @ExcelExport(justExport = true, name="Target Group")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToOne
    private TargetGroup targetGroup;

    public String getAddress() {
        return address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public TargetGroup getTargetGroup() {
        return targetGroup;
    }

    public void setTargetGroup(final TargetGroup targetGroup) {
        this.targetGroup = targetGroup;
    }
}
