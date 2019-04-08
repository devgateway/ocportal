package org.devgateway.toolkit.persistence.service.category;

import org.devgateway.toolkit.persistence.dao.categories.ChargeAccount;
import org.devgateway.toolkit.persistence.service.BaseJpaService;
import org.devgateway.toolkit.persistence.service.TextSearchableService;

/**
 * @author gmutuhu
 *
 */
public interface ChargeAccountService extends BaseJpaService<ChargeAccount>, TextSearchableService<ChargeAccount> {

}
