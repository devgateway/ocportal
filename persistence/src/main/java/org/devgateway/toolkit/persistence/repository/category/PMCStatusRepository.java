/**
 *
 */
package org.devgateway.toolkit.persistence.repository.category;

import org.devgateway.toolkit.persistence.dao.categories.PMCStatus;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author mpostelnicu
 *
 */
@Transactional
public interface PMCStatusRepository extends CategoryRepository<PMCStatus> {
}

