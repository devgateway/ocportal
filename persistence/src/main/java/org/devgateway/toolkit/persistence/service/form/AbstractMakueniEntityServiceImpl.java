package org.devgateway.toolkit.persistence.service.form;

import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.dao.form.AbstractMakueniEntity;
import org.devgateway.toolkit.persistence.repository.form.AbstractMakueniEntityRepository;
import org.devgateway.toolkit.persistence.service.BaseJpaServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
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
    @Cacheable
    public List<T> findByFiscalYear(final FiscalYear fiscalYear) {
        return makueniRepository().findByFiscalYear(fiscalYear);
    }


    public Collection<? extends AbstractMakueniEntity> getAllChildrenInHierarchy(T entity) {
        T loadedEntity = findById(entity.getId()).orElseThrow(
                () -> new RuntimeException("Cannot find entity with id " + entity.getId()));
        return loadedEntity.getDirectChildrenEntities().stream().filter(Objects::nonNull)
                .flatMap(s -> Stream.concat(Stream.of(s), s.getDirectChildrenEntities().stream()))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    @Override
    public Stream<? extends AbstractMakueniEntity> getAllSubmitted() {
        return makueniRepository().findByStatus(DBConstants.Status.SUBMITTED);
    }
}
