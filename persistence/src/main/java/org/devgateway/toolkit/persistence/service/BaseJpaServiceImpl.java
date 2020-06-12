package org.devgateway.toolkit.persistence.service;

import org.devgateway.toolkit.persistence.dao.GenericPersistable;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author idobre
 * @since 2019-03-04
 */
public abstract class BaseJpaServiceImpl<T extends GenericPersistable & Serializable> implements BaseJpaService<T> {

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
    @Transactional(readOnly = false)
    // we don't need cache here!
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
    @Transactional(readOnly = false)
    public <S extends T> S save(final S entity) {
        return repository().save(entity);
    }

    @Override
    @Transactional(readOnly = false)
    public <S extends T> List<S> saveAll(final Iterable<S> entities) {
        return repository().saveAll(entities);
    }

    @Override
    @Transactional(readOnly = false)
    public <S extends T> S saveAndFlush(final S entity) {
        return repository().saveAndFlush(entity);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(final T entity) {
        repository().delete(entity);
    }

    protected abstract BaseJpaRepository<T, Long> repository();

}
