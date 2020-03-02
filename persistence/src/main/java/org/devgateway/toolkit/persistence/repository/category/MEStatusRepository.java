/**
 *
 */
package org.devgateway.toolkit.persistence.repository.category;

import org.devgateway.toolkit.persistence.dao.categories.MEStatus;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author mpostelnicu
 *
 */
@Transactional
public interface MEStatusRepository extends CategoryRepository<MEStatus> {
}

