/**
 *
 */
package org.devgateway.toolkit.persistence.service.category;

import org.devgateway.toolkit.persistence.dao.categories.ProjectClosureHandover;
import org.devgateway.toolkit.persistence.service.BaseJpaService;
import org.devgateway.toolkit.persistence.service.TextSearchableService;

/**
 * @author mpostelnicu
 *
 */
public interface ProjectClosureHandoverService extends BaseJpaService<ProjectClosureHandover>,
        TextSearchableService<ProjectClosureHandover> {

}
