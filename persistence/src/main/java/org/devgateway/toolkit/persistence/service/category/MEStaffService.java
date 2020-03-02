/**
 *
 */
package org.devgateway.toolkit.persistence.service.category;

import org.devgateway.toolkit.persistence.dao.categories.MEStaff;
import org.devgateway.toolkit.persistence.service.BaseJpaService;
import org.devgateway.toolkit.persistence.service.TextSearchableService;

/**
 * @author mpostelnicu
 *
 */
public interface MEStaffService extends BaseJpaService<MEStaff>, TextSearchableService<MEStaff> {

}
