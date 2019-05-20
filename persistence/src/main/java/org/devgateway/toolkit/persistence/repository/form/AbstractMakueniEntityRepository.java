package org.devgateway.toolkit.persistence.repository.form;


import org.devgateway.toolkit.persistence.dao.form.AbstractMakueniEntity;
import org.devgateway.toolkit.persistence.repository.norepository.TextSearchableRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.util.List;

@NoRepositoryBean
public interface AbstractMakueniEntityRepository<T extends AbstractMakueniEntity>
        extends TextSearchableRepository<T, Long> {
    List<T> findByFiscalYearId(Long id);

    @Override
    @Query("select c from  #{#entityName} c")
    Page<T> searchText(@Param("name") String name, Pageable page);
}
