package org.devgateway.toolkit.persistence.service.form;

import java.util.List;

import org.devgateway.toolkit.persistence.dao.Person;
import org.devgateway.toolkit.persistence.dao.form.AbstractMakueniEntity;
import org.devgateway.toolkit.persistence.dao.form.Lockable;

public interface MakueniEntityServiceResolver {

     void unlock(Lockable entity);

     List<? extends AbstractMakueniEntity> getAllLocked(Person person);

     List<? extends AbstractMakueniEntity> getAllLocked();
}
