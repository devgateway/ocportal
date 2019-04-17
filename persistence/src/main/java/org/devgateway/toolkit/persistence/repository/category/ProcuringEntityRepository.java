/**
 * 
 */
package org.devgateway.toolkit.persistence.repository.category;

import org.devgateway.toolkit.persistence.dao.categories.ProcuringEntity;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author gmutuhu
 *
 */
@Transactional
public interface ProcuringEntityRepository extends CategoryRepository<ProcuringEntity> {
}

