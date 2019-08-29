package org.devgateway.toolkit.persistence.repository.category;

import org.devgateway.toolkit.persistence.dao.categories.Unit;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author idobre
 * @since 26/08/2019
 */
@Transactional
public interface UnitRepository extends CategoryRepository<Unit> {

}
