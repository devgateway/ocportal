package org.devgateway.toolkit.persistence.service.form;

import org.devgateway.toolkit.persistence.dao.form.AbstractMakueniEntity;

public interface MakueniEntityServiceResolver {

     <S extends AbstractMakueniEntity> S saveAndFlushMakueniEntity(S entity);
}
