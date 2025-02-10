package org.devgateway.toolkit.persistence.service.form;

import java.util.List;

import org.devgateway.toolkit.persistence.dao.Person;
import org.devgateway.toolkit.persistence.dao.form.AbstractClientEntity;
import org.devgateway.toolkit.persistence.dao.form.Lockable;

public interface ClientEntityServiceResolver {

     void unlock(Lockable entity);

     List<? extends AbstractClientEntity> getAllLocked(Person person);

     List<? extends AbstractClientEntity> getAllLocked();
}
