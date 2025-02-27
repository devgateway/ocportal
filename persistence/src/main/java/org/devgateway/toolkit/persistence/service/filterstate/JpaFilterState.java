package org.devgateway.toolkit.persistence.service.filterstate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.danekja.java.util.function.serializable.SerializableFunction;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.ListJoin;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.SetJoin;
import jakarta.persistence.criteria.Subquery;
import jakarta.persistence.metamodel.ListAttribute;
import jakarta.persistence.metamodel.SetAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import java.io.Serializable;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Created by Octavian on 01.07.2016.
 */
public class JpaFilterState<T> implements Serializable {

    private static final long serialVersionUID = 2241550275925712593L;

    protected <C> Predicate subListLike(final Root<T> root, final CriteriaBuilder cb, final CriteriaQuery<?> query,
                                        final Class<T> parentClazz, final ListAttribute<T, C> listAttribute,
                                        final SingularAttribute<C, String> itemLikeAttribute, final String like) {
        Subquery<T> sq = query.subquery(parentClazz).distinct(true);
        Root<T> subRoot = sq.from(parentClazz);
        ListJoin<T, C> subOrigins = subRoot.join(listAttribute);
        return root.in(sq.where(cb.like(cb.lower(subOrigins.get(itemLikeAttribute)),
                "%" + like.toLowerCase() + "%")).select(subRoot));
    }

    protected <C, ID extends Serializable> Predicate subListIn(final Root<T> root, final CriteriaQuery<?> query,
                                                               final Class<T> parentClazz, final ListAttribute<T, C>
                                                                       listAttribute,
                                                               final SingularAttribute<? super C, ID> idAttr,
                                                               final SerializableFunction<C, ID> idSupplier,
                                                               final Collection<C> items) {
        Subquery<T> sq = query.subquery(parentClazz).distinct(true);
        Root<T> subRoot = sq.from(parentClazz);
        ListJoin<T, C> sub = subRoot.join(listAttribute);
        return root.in(sq.where(sub.get(idAttr).in(items
                .stream().map(idSupplier).collect(Collectors.toList()))).select(subRoot));
    }

    protected <C, ID extends Serializable> Predicate subSetIn(final Root<T> root, final CriteriaQuery<?> query,
                                                              final Class<T> parentClazz,
                                                              final SetAttribute<T, C> setAttribute,
                                                              final SingularAttribute<? super C, ID> idAttr,
                                                              final SerializableFunction<C, ID> idSupplier,
                                                              final Collection<C> items) {
        Subquery<T> sq = query.subquery(parentClazz).distinct(true);
        Root<T> subRoot = sq.from(parentClazz);
        SetJoin<T, C> sub = subRoot.join(setAttribute);
        return root.in(sq.where(sub.get(idAttr).in(items
                .stream().map(idSupplier).collect(Collectors.toList()))).select(subRoot));
    }


    @JsonIgnore
    public Specification<T> getSpecification() {
        return (root, query, cb) -> cb.and();
    }
}
