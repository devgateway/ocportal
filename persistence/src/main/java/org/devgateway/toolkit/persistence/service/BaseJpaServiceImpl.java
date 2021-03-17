package org.devgateway.toolkit.persistence.service;

import com.google.common.collect.ImmutableSet;
import net.sf.ehcache.CacheManager;
import org.devgateway.toolkit.persistence.dao.GenericPersistable;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.devgateway.toolkit.persistence.validator.groups.HighLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author idobre
 * @since 2019-03-04
 */
public abstract class BaseJpaServiceImpl<T extends GenericPersistable & Serializable> implements BaseJpaService<T> {

    @Autowired
    private Validator validator;

    @Override
    public List<T> findAll() {
        return repository().findAll();
    }

    @Override
    public List<T> findAll(final Sort sort) {
        return repository().findAll(sort);
    }

    @Override
    public List<T> findAll(final Specification<T> spec) {
        return repository().findAll(spec);
    }

    @Override
    public List<T> findAllNoCache(final Specification<T> spec) {
        return repository().findAll(spec);
    }

    @Override
    public Page<T> findAll(final Specification<T> spec, final Pageable pageable) {
        return repository().findAll(spec, pageable);
    }

    @Override
    public Page<T> findAllNoCache(final Specification<T> spec, final Pageable pageable) {
        return repository().findAll(spec, pageable);
    }

    @Override
    public Optional<T> findOne(final Specification<T> spec) {
        return repository().findOne(spec);
    }

    @Override
    public Page<T> findAll(final Pageable pageable) {
        return repository().findAll(pageable);
    }

    @Override
    public List<T> findAll(final Specification<T> spec, final Sort sort) {
        return repository().findAll(spec, sort);
    }

    @Override
    public long count(final Specification<T> spec) {
        return repository().count(spec);
    }

    @Override
    public long countNoCache(final Specification<T> spec) {
        return repository().count(spec);
    }

    @Override
    @Transactional
    public Optional<T> findById(final Long id) {
        return repository().findById(id);
    }

    @Override
    public Set<T> findAllById(Iterable<Long> ids) {
        return new HashSet<>(repository().findAllById(ids));
    }

    @Override
    public long count() {
        return repository().count();
    }

    @Override
    @Transactional
    public <S extends T> S save(final S entity) {
        assertEntityIsValid(entity);
        S save = repository().save(entity);
        flushRelatedCollectionCaches();
        return save;
    }

    @Override
    @Transactional(readOnly = false)
    public <S extends T> List<S> saveAll(final Iterable<S> entities) {
        for (S entity : entities) {
            assertEntityIsValid(entity);
        }
        List<S> s = repository().saveAll(entities);
        flushRelatedCollectionCaches();
        return s;
    }

    @Override
    @Transactional
    public <S extends T> S saveAndFlush(final S entity) {
        assertEntityIsValid(entity);
        S s = repository().saveAndFlush(entity);
        flushRelatedCollectionCaches();
        return s;
    }

    private <S extends T> void assertEntityIsValid(S entity) {
        Set<ConstraintViolation<S>> violations = validator.validate(entity, Default.class, HighLevel.class);
        if (!violations.isEmpty()) {
            throw new InvalidObjectException(new HashSet<>(violations));
        }
    }

    @Override
    @Transactional
    public void delete(final T entity) {
        repository().delete(entity);
        flushRelatedCollectionCaches();
    }

    @Override
    public Collection<String> getRelatedCollectionCaches() {
        return null;
    }

    @Override
    public void flushRelatedCollectionCaches() {
        CacheManager cm = CacheManager.getInstance();
        if (cm != null && getRelatedCollectionCaches() != null) {
            getRelatedCollectionCaches().forEach(cm::clearAllStartingWith);
        }
    }

    protected abstract BaseJpaRepository<T, Long> repository();
}
