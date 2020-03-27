package org.devgateway.toolkit.persistence.service;

import com.google.common.collect.Sets;
import org.apache.logging.log4j.util.Strings;
import org.devgateway.toolkit.persistence.dao.Person;
import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.repository.PersonRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.devgateway.toolkit.persistence.repository.norepository.TextSearchableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author idobre
 * @since 2019-03-04
 */
@Service
@Transactional
public class PersonServiceImpl extends BaseJpaServiceImpl<Person> implements PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Override
    @Cacheable
    public Person findByUsername(final String username) {
        return personRepository.findByUsername(username);
    }

    @Override
    @Cacheable
    public Person findByEmail(final String email) {
        return personRepository.findByEmail(email);
    }

    @Override
    public List<Person> findByDepartmentWithRoles(Department department, String... roles) {
        return personRepository.findByDepartmentAndRoleIn(department, Sets.newHashSet(roles));
    }

    @Override
    public List<Person> findByRoleIn(String... roles) {
        return personRepository.findByRoleIn(Sets.newHashSet(roles));
    }

    @Override
    protected BaseJpaRepository<Person, Long> repository() {
        return personRepository;
    }

    @Override
    public Person newInstance() {
        return new Person();
    }

    @Override
    public TextSearchableRepository<Person, Long> textRepository() {
        return personRepository;
    }

    @Override
    public Set<String> getEmailsByRole(String role) {
        return findByRoleIn(role).stream().map(Person::getEmail)
                .map(Strings::trimToNull).filter(Objects::nonNull).collect(Collectors.toSet());
    }
}
