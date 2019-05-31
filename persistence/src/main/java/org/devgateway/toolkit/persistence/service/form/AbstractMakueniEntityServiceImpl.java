package org.devgateway.toolkit.persistence.service.form;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.dao.form.AbstractMakueniEntity;
import org.devgateway.toolkit.persistence.repository.form.AbstractMakueniEntityRepository;
import org.devgateway.toolkit.persistence.service.BaseJpaServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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


    public Collection<? extends AbstractMakueniEntity> getAllChildrenInHierarchy(T entity) {
        T loadedEntity = findById(entity.getId()).get();
        return loadedEntity.getDirectChildrenEntities().stream().filter(Objects::nonNull)
                .flatMap(s -> Stream.concat(Stream.of(s), s.getDirectChildrenEntities().stream()))
                .collect(Collectors.toSet());
    }
}
