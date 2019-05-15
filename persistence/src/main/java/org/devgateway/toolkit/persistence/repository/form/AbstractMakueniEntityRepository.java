package org.devgateway.toolkit.persistence.repository.form;


import org.devgateway.toolkit.persistence.dao.form.AbstractMakueniEntity;
import org.devgateway.toolkit.persistence.repository.norepository.TextSearchableRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface AbstractMakueniEntityRepository<T extends AbstractMakueniEntity> extends TextSearchableRepository<T, Long> {
    List<T> findByProcurementPlanFiscalYearId(Long id);
}
