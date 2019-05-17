package org.devgateway.toolkit.persistence.repository.form;


import org.devgateway.toolkit.persistence.dao.form.AbstractPurchaseReqMakueniEntity;
import org.devgateway.toolkit.persistence.dao.form.PurchaseRequisition;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.util.List;

@NoRepositoryBean
public interface AbstractPurchaseReqMakueniEntityRepository<T extends AbstractPurchaseReqMakueniEntity>
        extends AbstractMakueniEntityRepository<T> {
    List<T> findByPurchaseRequisition(@Param("purchaseRequisition") PurchaseRequisition purchaseRequisition);

    @Query("select c from  #{#entityName} c "
            + " where c.purchaseRequisition.project.procurementPlan.fiscalYear.id = :fiscalYearId")
    List<T> findByFiscalYearId(@Param("fiscalYearId") Long  id);
}
