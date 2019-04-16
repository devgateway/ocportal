/**
 * 
 */
package org.devgateway.toolkit.persistence.service.category;

import org.devgateway.toolkit.persistence.dao.categories.Supplier;
import org.devgateway.toolkit.persistence.service.BaseJpaService;
import org.devgateway.toolkit.persistence.service.TextSearchableService;

/**
 * @author gmutuhu
 *
 */
public interface SupplierService extends BaseJpaService<Supplier>, TextSearchableService<Supplier> {

}
