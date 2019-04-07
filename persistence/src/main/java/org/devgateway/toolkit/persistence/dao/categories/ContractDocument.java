/**
 * 
 */
package org.devgateway.toolkit.persistence.dao.categories;

import javax.persistence.Entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

/**
 * @author gmutuhu
 *
 */
@Entity
@Audited
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ContractDocument extends Category {

}
