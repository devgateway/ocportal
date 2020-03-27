/**
 *
 */
package org.devgateway.toolkit.persistence.repository.category;

import org.devgateway.toolkit.persistence.dao.categories.ProjectClosureHandover;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author mpostelnicu
 *
 */
@Transactional
public interface ProjectClosureHandoverRepository extends CategoryRepository<ProjectClosureHandover> {
}

