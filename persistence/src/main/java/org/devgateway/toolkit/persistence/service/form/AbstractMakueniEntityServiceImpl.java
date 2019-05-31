package org.devgateway.toolkit.persistence.service.form;

import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.dao.form.AbstractMakueniEntity;
import org.devgateway.toolkit.persistence.repository.form.AbstractMakueniEntityRepository;
import org.devgateway.toolkit.persistence.service.BaseJpaServiceImpl;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * @author idobre
 * @since 2019-05-21
 */
public abstract class AbstractMakueniEntityServiceImpl<T extends AbstractMakueniEntity>
        extends BaseJpaServiceImpl<T> implements AbstractMakueniEntityService<T> {

    @Override
    // @Cacheable
    public List<T> findByFiscalYear(final FiscalYear fiscalYear) {
        final AbstractMakueniEntityRepository repository = (AbstractMakueniEntityRepository) repository();
        return repository.findByFiscalYear(fiscalYear);
    }
}
