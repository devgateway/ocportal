package org.devgateway.toolkit.persistence.service.filterstate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.danekja.java.util.function.serializable.SerializableFunction;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;
import javax.persistence.criteria.Subquery;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
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
