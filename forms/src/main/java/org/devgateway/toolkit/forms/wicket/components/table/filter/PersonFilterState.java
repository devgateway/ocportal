package org.devgateway.toolkit.forms.wicket.components.table.filter;

import org.apache.commons.lang3.StringUtils;
import org.devgateway.toolkit.persistence.dao.Person;
import org.devgateway.toolkit.persistence.dao.Person_;
import org.devgateway.toolkit.persistence.dao.Role;
import org.devgateway.toolkit.persistence.dao.Role_;
import org.devgateway.toolkit.persistence.dao.categories.Department;
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

    private Department department;

    private List<Role> roles;

    @Override
    public Specification<Person> getSpecification() {
        return (root, query, cb) -> {
            final List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.isNotBlank(username)) {
                predicates.add(cb.like(root.get(Person_.username), "%" + username + "%"));
            }

            if (department != null) {
                predicates.add(cb.equal(root.get(Person_.department), department));
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

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(final Department department) {
        this.department = department;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(final List<Role> roles) {
        this.roles = roles;
    }
}
