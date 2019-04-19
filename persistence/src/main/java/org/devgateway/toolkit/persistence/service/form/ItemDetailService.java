package org.devgateway.toolkit.persistence.service.form;

import org.devgateway.toolkit.persistence.dao.form.ItemDetail;
import org.devgateway.toolkit.persistence.service.BaseJpaService;
import org.devgateway.toolkit.persistence.service.TextSearchableService;

public interface ItemDetailService extends BaseJpaService<ItemDetail>, TextSearchableService<ItemDetail> {

}
