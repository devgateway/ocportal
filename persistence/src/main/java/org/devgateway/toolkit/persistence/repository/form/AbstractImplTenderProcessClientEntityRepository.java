package org.devgateway.toolkit.persistence.repository.form;


import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.dao.form.AbstractImplTenderProcessClientEntity;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.util.List;

@NoRepositoryBean
public interface AbstractImplTenderProcessClientEntityRepository<T extends AbstractImplTenderProcessClientEntity>
        extends AbstractClientEntityRepository<T> {
    List<T> findByTenderProcess(@Param("tenderProcess") TenderProcess tenderProcess);

    @Override
    @Query("select c from  #{#entityName} c "
            + " where c.tenderProcess.procurementPlan.fiscalYear = :fiscalYear")
    List<T> findByFiscalYear(@Param("fiscalYear") FiscalYear fiscalYear);
}
