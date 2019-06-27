package org.devgateway.toolkit.persistence.repository.category;

import org.devgateway.toolkit.persistence.dao.categories.Staff;
import org.devgateway.toolkit.persistence.dao.categories.Ward;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author idobre
 * @since 2019-06-26
 */
@Transactional
public interface WardRepository extends CategoryRepository<Ward> {
}
