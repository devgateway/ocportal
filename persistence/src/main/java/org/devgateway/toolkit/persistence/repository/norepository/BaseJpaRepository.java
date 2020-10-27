package org.devgateway.toolkit.persistence.repository.norepository;

import org.devgateway.toolkit.persistence.repository.CacheHibernateQueryResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Octavian on 01.07.2016.
 */
@NoRepositoryBean
public interface BaseJpaRepository<T, ID extends Serializable>
        extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

    @Override
    @CacheHibernateQueryResult
    List<T> findAll();
}
