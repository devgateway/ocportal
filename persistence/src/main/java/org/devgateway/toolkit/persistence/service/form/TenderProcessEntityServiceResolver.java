package org.devgateway.toolkit.persistence.service.form;

import org.devgateway.toolkit.persistence.dao.form.AbstractTenderProcessClientEntity;

/**
 * @author Octavian Ciubotaru
 */
public interface TenderProcessEntityServiceResolver {

    <T extends AbstractTenderProcessClientEntity> long countByTenderProcess(T entity);

    <T extends AbstractTenderProcessClientEntity> T saveAndFlush(T entity);
}
