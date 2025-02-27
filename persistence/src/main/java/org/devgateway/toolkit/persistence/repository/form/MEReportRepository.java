package org.devgateway.toolkit.persistence.repository.form;

import org.devgateway.toolkit.persistence.dao.form.MEReport;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface MEReportRepository extends AbstractImplTenderProcessClientEntityRepository<MEReport> {
}
