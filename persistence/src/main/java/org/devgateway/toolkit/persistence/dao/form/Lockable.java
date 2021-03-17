package org.devgateway.toolkit.persistence.dao.form;

import java.io.Serializable;

import org.devgateway.toolkit.persistence.dao.Labelable;
import org.devgateway.toolkit.persistence.dao.Person;
import org.springframework.data.domain.Persistable;

/**
 * Used to mark entities that participate in pessimistic lock. Can tell if user is checked by a person (owner) or not.
 *
 * @author Octavian Ciubotaru
 */
public interface Lockable extends Labelable, Persistable<Long>, Serializable {

    Person getOwner();

    void setOwner(Person owner);
}
