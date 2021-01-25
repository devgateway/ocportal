package org.devgateway.toolkit.persistence.service.form;

import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.dao.form.AbstractMakueniEntity;
import org.devgateway.toolkit.persistence.repository.form.AbstractMakueniEntityRepository;
import org.devgateway.toolkit.persistence.service.BaseJpaServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * @author idobre
 * @since 2019-05-21
 */
public abstract class AbstractMakueniEntityServiceImpl<T extends AbstractMakueniEntity>
        extends BaseJpaServiceImpl<T> implements AbstractMakueniEntityService<T> {


    @Transactional
    public AbstractMakueniEntityRepository<T> makueniRepository() {
        return (AbstractMakueniEntityRepository<T>) repository();
    }

    @Override
    public List<T> findByFiscalYear(final FiscalYear fiscalYear) {
        return makueniRepository().findByFiscalYear(fiscalYear);
    }

    @Override
    public Stream<? extends AbstractMakueniEntity> getAllSubmitted() {
        return makueniRepository().findByStatus(DBConstants.Status.SUBMITTED);
    }
}
