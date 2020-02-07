package org.devgateway.toolkit.persistence.repository.form;

import org.devgateway.toolkit.persistence.dao.form.PMCReport;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface PMCReportRepository extends AbstractImplTenderProcessMakueniEntityRepository<PMCReport> {
}
