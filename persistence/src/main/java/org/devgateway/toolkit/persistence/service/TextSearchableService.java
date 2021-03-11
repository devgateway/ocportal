package org.devgateway.toolkit.persistence.service;

import org.devgateway.toolkit.persistence.dao.GenericPersistable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.metamodel.SingularAttribute;
import java.io.Serializable;
import java.util.Optional;


public interface TextSearchableService<T extends GenericPersistable & Serializable> {

    Page<T> findAll(Pageable pageable);

    Page<T> findAll(Specification<T> spec, Pageable pageable);

    <S extends T> S save(S entity);

    Optional<T> findById(Long id);

    SingularAttribute<? super T, String> getTextAttribute();

    default Specification<T> getTextSpecification(String text) {
        return (root, query, cb) -> cb.like(cb.lower(root.get(getTextAttribute())), "%" + text.toLowerCase() + "%");
    }

    default Specification<T> getTextCombinedSpecification(Specification<T> specification,
                                                          Specification<T> textSpecFunction) {
        return specification == null ? textSpecFunction : specification.and(textSpecFunction);
    }

    default Page<T> findAllByTextCombinedSpecification(Specification<T> specification,
                                                       Specification<T> textSpecification,
                                                       PageRequest pageRequest) {
        return findAll(getTextCombinedSpecification(specification, textSpecification), pageRequest);
    }
}
