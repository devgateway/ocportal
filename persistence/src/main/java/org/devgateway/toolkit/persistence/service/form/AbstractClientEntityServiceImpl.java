package org.devgateway.toolkit.persistence.service.form;

import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.Person;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.dao.form.AbstractClientEntity;
import org.devgateway.toolkit.persistence.repository.form.AbstractClientEntityRepository;
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
public abstract class AbstractClientEntityServiceImpl<T extends AbstractClientEntity>
        extends BaseJpaServiceImpl<T> implements AbstractClientEntityService<T> {


    @Transactional
    public AbstractClientEntityRepository<T> makueniRepository() {
        return (AbstractClientEntityRepository<T>) repository();
    }

    @Override
    public List<T> findByFiscalYear(final FiscalYear fiscalYear) {
        return makueniRepository().findByFiscalYear(fiscalYear);
    }

    @Override
    public Stream<? extends AbstractClientEntity> getAllSubmitted() {
        return makueniRepository().findByStatus(DBConstants.Status.SUBMITTED);
    }

    @Override
    public List<T> getAllLocked(Person person) {
        return makueniRepository().getAllLockedByPerson(person);
    }

    @Override
    public List<T> getAllLocked() {
        return makueniRepository().getAllLocked();
    }

    @Override
    @Transactional
    public void unlock(Long id) {
        T one = makueniRepository().getOne(id);
        one.setOwner(null);
    }
}
