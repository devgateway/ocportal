package org.devgateway.toolkit.persistence.service;

import org.devgateway.toolkit.persistence.dao.Person;
import org.devgateway.toolkit.persistence.dao.categories.Department;

import java.util.List;
import java.util.Set;

public interface PersonService extends BaseJpaService<Person>, TextSearchableService<Person> {
    Person findByUsername(String username);

    Person findByEmail(String email);

    List<Person> findByDepartmentWithRoles(Department department, String... roles);

    List<Person> findByRoleIn(String... roles);

    Set<String> getEmailsByRole(String role);
}
