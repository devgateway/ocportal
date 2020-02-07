package org.devgateway.toolkit.persistence.repository.form;

import org.devgateway.toolkit.persistence.dao.form.AdministratorReport;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface AdministratorReportRepository extends AbstractImplTenderProcessMakueniEntityRepository
        <AdministratorReport> {
}
