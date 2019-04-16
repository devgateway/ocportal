/**
 * 
 */
package org.devgateway.toolkit.persistence.repository.category;

import org.devgateway.toolkit.persistence.dao.categories.Supplier;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author gmutuhu
 *
 */
@Transactional
public interface SupplierRepository extends CategoryRepository<Supplier> {
}

