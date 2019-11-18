package org.devgateway.toolkit.persistence.service.form;

import org.devgateway.toolkit.persistence.dao.form.AwardNotification;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess;

/**
 * @author gmutuhu
 */
public interface AwardNotificationService extends AbstractMakueniEntityService<AwardNotification> {
    AwardNotification findByTenderProcess(TenderProcess tenderProcess);
}
