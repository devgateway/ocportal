package org.devgateway.toolkit.persistence.repository.form;

import org.devgateway.toolkit.persistence.dao.form.InspectionReport;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface InspectionReportRepository extends AbstractPurchaseReqMakueniEntityRepository<InspectionReport> {
}
