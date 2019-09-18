package org.devgateway.toolkit.persistence.service.filterstate;

import org.apache.commons.lang3.StringUtils;
import org.devgateway.toolkit.persistence.dao.Person;
import org.devgateway.toolkit.persistence.dao.Person_;
import org.devgateway.toolkit.persistence.dao.Role;
import org.devgateway.toolkit.persistence.dao.Role_;
import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.categories.Department_;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author idobre
 * @since 2019-03-11
 */
public class PersonFilterState extends JpaFilterState<Person> {
    private String username;

    private List<Department> departments;

    private List<Role> roles;

    @Override
    public Specification<Person> getSpecification() {
        return (root, query, cb) -> {
            final List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.isNotBlank(username)) {
                predicates.add(cb.like(root.get(Person_.username), "%" + username + "%"));
            }

            if (departments != null && !departments.isEmpty()) {
                predicates.add(root.join(Person_.departments, JoinType.LEFT).get(Department_.id)
                        .in(departments.stream().map(dep -> dep.getId()).collect(Collectors.toList())));
                query.distinct(true);
            }

            if (roles != null && !roles.isEmpty()) {
                predicates.add(root.join(Person_.roles, JoinType.LEFT).get(Role_.authority)
                        .in(roles.stream().map(role -> role.getAuthority()).collect(Collectors.toList())));
                query.distinct(true);
            }

            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public List<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(final List<Department> departments) {
        this.departments = departments;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(final List<Role> roles) {
        this.roles = roles;
    }
}
