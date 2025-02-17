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
package org.devgateway.toolkit.persistence.dao;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import nl.dries.wicket.hibernate.dozer.proxy.Proxied;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * @author mpostelnicu, ociubotaru
 */
@JsonIgnoreProperties(value = {"new"})
@MappedSuperclass
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class GenericPersistable extends AbstractPersistable<Long> implements Serializable {

    @Version
    @Column(name = "optlock", columnDefinition = "integer default 0")
    private Integer version;

    /**
     * Custom serialization for id is needed since Spring Data JPA 2.x AbstractPersistable no longer implements
     * Serializable.
     */


    public Integer getVersion() {
        return version;
    }
}
