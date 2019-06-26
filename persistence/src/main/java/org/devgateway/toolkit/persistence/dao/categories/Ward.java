package org.devgateway.toolkit.persistence.dao.categories;

import org.devgateway.toolkit.persistence.excel.annotation.ExcelExport;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * @author idobre
 * @since 2019-06-26
 */
@Entity
@Audited
public class Ward extends Category {

    @ExcelExport(justExport = true)
    @ManyToOne
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinColumn(name = "subcounty_id")
    private Subcounty subcounty;
}
