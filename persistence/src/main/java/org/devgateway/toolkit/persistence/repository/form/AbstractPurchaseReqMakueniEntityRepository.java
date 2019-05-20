package org.devgateway.toolkit.persistence.repository.form;


import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.dao.form.AbstractPurchaseReqMakueniEntity;
import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisition;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.util.List;

@NoRepositoryBean
public interface AbstractPurchaseReqMakueniEntityRepository<T extends AbstractPurchaseReqMakueniEntity>
        extends AbstractMakueniEntityRepository<T> {
    T findByPurchaseRequisition(@Param("purchaseRequisition") PurchaseRequisition purchaseRequisition);

    @Override
    @Query("select c from  #{#entityName} c "
            + " where c.purchaseRequisition.project.procurementPlan.fiscalYear = :fiscalYear")
    List<T> findByFiscalYear(@Param("fiscalYear") FiscalYear fiscalYear);
}
