package org.devgateway.toolkit.persistence.repository.category;

import org.devgateway.toolkit.persistence.dao.categories.ProcurementMethod;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author idobre
 * @since 2019-03-11
 */
@Transactional
public interface ProcurementMethodRepository extends CategoryRepository<ProcurementMethod> {

}
