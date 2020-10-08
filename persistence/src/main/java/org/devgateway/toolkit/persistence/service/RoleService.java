package org.devgateway.toolkit.persistence.service;

import org.devgateway.toolkit.persistence.dao.Role;

import java.util.Collection;
import java.util.List;

public interface RoleService extends BaseJpaService<Role>, TextSearchableService<Role> {

    Role findByAuthority(String authority);

    List<Role> findByAuthorityIn(Collection<String> authority);
}
