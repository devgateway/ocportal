package org.devgateway.toolkit.persistence.repository.category;

import org.devgateway.toolkit.persistence.dao.categories.TargetGroup;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author idobre
 * @since 2019-03-11
 */
@Transactional
public interface TargetGroupRepository extends CategoryRepository<TargetGroup> {

}
