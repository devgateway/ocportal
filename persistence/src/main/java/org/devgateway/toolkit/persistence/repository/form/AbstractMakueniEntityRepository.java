package org.devgateway.toolkit.persistence.repository.form;


import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.dao.form.AbstractMakueniEntity;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.stream.Stream;

@NoRepositoryBean
public interface AbstractMakueniEntityRepository<T extends AbstractMakueniEntity>
        extends BaseJpaRepository<T, Long> {
    List<T> findByFiscalYear(FiscalYear fiscalYear);

    Stream<T> findByStatus(String status);
}
