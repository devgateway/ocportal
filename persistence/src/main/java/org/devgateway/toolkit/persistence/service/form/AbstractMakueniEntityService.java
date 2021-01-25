package org.devgateway.toolkit.persistence.service.form;

import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.dao.form.AbstractMakueniEntity;
import org.devgateway.toolkit.persistence.service.BaseJpaService;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author idobre
 * @since 2019-05-21
 */
public interface AbstractMakueniEntityService<T extends AbstractMakueniEntity> extends BaseJpaService<T> {
    List<T> findByFiscalYear(FiscalYear fiscalYear);

    Stream<? extends AbstractMakueniEntity> getAllSubmitted();

    //Collection<Map<String, String>> validate(TenderProcess tenderProcess, T entity);
}
