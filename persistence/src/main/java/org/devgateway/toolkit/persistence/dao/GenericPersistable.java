/**
 * Copyright (c) 2015 Development Gateway, Inc and others.
 * <p>
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License (MIT)
 * which accompanies this distribution, and is available at
 * https://opensource.org/licenses/MIT
 * <p>
 * Contributors:
 * Development Gateway - initial API and implementation
 */
/**
 *
 */
package org.devgateway.toolkit.persistence.dao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.io.Serializable;

/**
 * @author mpostelnicu
 */
@JsonIgnoreProperties(value = {"new"})
public class GenericPersistable extends AbstractPersistable<Long> implements Serializable {

//    /**
//     * Custom serialization for id is needed since Spring Data JPA 2.x AbstractPersistable no longer implements
//     * Serializable.
//     */
//    private void writeObject(final ObjectOutputStream out) throws IOException {
//        out.writeObject(getId());
//        out.defaultWriteObject();
//    }
//
//    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
//        Long id = (Long) in.readObject();
//
//        // If this entity was proxied by dozer-hibernate-model then do not restore the id since it will raise a
//        // NullPointerException. The other properties were not stored anyway.
//        if (!(this instanceof Proxied)) {
//            setId(id);
//        }
//
//        in.defaultReadObject();
//    }
}
