package org.devgateway.toolkit.persistence.repository.form;

import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisitionGroup;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author mpostelnicu
 *
 */
@Transactional
public interface PurchaseRequisitionGroupRepository extends AbstractTenderProcessMakueniEntityRepository
        <PurchaseRequisitionGroup> {

}
