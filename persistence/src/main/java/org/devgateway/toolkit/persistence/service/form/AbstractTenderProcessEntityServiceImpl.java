package org.devgateway.toolkit.persistence.service.form;

import org.devgateway.toolkit.persistence.dao.form.AbstractTenderProcessClientEntity;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess;
import org.devgateway.toolkit.persistence.repository.form.AbstractTenderProcessClientEntityRepository;

/**
 * @author Octavian Ciubotaru
 */
public abstract class AbstractTenderProcessEntityServiceImpl<T extends AbstractTenderProcessClientEntity>
        extends AbstractClientEntityServiceImpl<T>
        implements AbstractTenderProcessEntityService<T> {

    @Override
    public long countByTenderProcess(Long entityId, TenderProcess tenderProcess) {
        AbstractTenderProcessClientEntityRepository repository =
                (AbstractTenderProcessClientEntityRepository) repository();
        return repository.countByTenderProcess(entityId == null ? -1 : entityId, tenderProcess);
    }
}
