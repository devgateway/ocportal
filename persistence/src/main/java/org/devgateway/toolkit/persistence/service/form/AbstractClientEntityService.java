package org.devgateway.toolkit.persistence.service.form;

import org.devgateway.toolkit.persistence.dao.Person;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.dao.form.AbstractClientEntity;
import org.devgateway.toolkit.persistence.service.BaseJpaService;
import org.devgateway.toolkit.persistence.service.LockableService;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author idobre
 * @since 2019-05-21
 */
public interface AbstractClientEntityService<T extends AbstractClientEntity>
        extends BaseJpaService<T>, LockableService<T> {

    List<T> findByFiscalYear(FiscalYear fiscalYear);

    Stream<? extends AbstractClientEntity> getAllSubmitted();

    List<T> getAllLocked(Person person);

    List<T> getAllLocked();
}
