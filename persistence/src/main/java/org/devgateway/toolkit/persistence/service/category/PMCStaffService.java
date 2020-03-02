/**
 *
 */
package org.devgateway.toolkit.persistence.service.category;

import org.devgateway.toolkit.persistence.dao.categories.PMCStaff;
import org.devgateway.toolkit.persistence.service.BaseJpaService;
import org.devgateway.toolkit.persistence.service.TextSearchableService;

/**
 * @author mpostelnicu
 *
 */
public interface PMCStaffService extends BaseJpaService<PMCStaff>, TextSearchableService<PMCStaff> {

}
