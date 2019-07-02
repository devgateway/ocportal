package org.devgateway.toolkit.persistence.repository.category;

import org.devgateway.toolkit.persistence.dao.categories.Subcounty;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author idobre
 * @since 2019-06-26
 */
@Transactional
public interface SubcountyRepository extends CategoryRepository<Subcounty> {
}
