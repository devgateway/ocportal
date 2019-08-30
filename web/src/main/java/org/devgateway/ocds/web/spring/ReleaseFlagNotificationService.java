package org.devgateway.ocds.web.spring;

import com.google.common.collect.Sets;
import org.devgateway.ocds.persistence.mongo.FlaggedRelease;
import org.devgateway.toolkit.persistence.dao.Person;
import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.service.PersonService;
import org.devgateway.toolkit.persistence.service.category.DepartmentService;
import org.devgateway.toolkit.web.security.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static org.devgateway.ocds.persistence.mongo.flags.FlagsConstants.FLAGS_LIST;

@Service
public class ReleaseFlagNotificationService {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private PersonService personService;

    private Map<String, Map<String, Set<String>>> departmentFlagRelease;


    @PostConstruct
    public void init() {
        departmentFlagRelease = new ConcurrentHashMap<>();
    }


    private void addDepartmentFlagReleaseId(String department, String flag, String releaseId) {
        departmentFlagRelease.putIfAbsent(department, new ConcurrentHashMap<>());
        Map<String, Set<String>> stringSetMap = departmentFlagRelease.get(department);
        stringSetMap.putIfAbsent(flag, new HashSet<>());
        Set<String> set = stringSetMap.get(flag);
        set.add(releaseId);
    }

    @Transactional
    private String getDepartmentNameFromRelease(FlaggedRelease flaggedRelease) {
        Optional<Department> department = departmentService.findById(flaggedRelease.getDepartmentId());
        if (!department.isPresent()) {
            return "Unknown";
        }
        return department.get().getLabel();
    }

    private Set<String> getUsersValidatorsEmailsFromRelease(FlaggedRelease flaggedRelease) {
        Optional<Department> department = departmentService.findById(flaggedRelease.getDepartmentId());
        if (!department.isPresent()) {
            return Sets.newHashSet();
        }

        return personService.findByDepartmentWithRoles(department.get(), SecurityConstants.Roles.ROLE_USER,
                SecurityConstants.Roles.ROLE_VALIDATOR
        ).stream().map(Person::getEmail).collect(Collectors.toSet());
    }

    private Set<String> getAdminsEmails() {
        return personService.findByRoleIn(SecurityConstants.Roles.ROLE_ADMIN).stream().map(Person::getEmail)
                .collect(Collectors.toSet());
    }

    public void addFlaggedReleaseToNotificationTree(FlaggedRelease flaggedRelease) {
        FLAGS_LIST.forEach(f -> {
                    boolean flag = flaggedRelease.getFlags().getFlagSet(f);
                    if (flag) {
                        addDepartmentFlagReleaseId(
                                getDepartmentNameFromRelease(flaggedRelease),
                                f,
                                flaggedRelease.getId()
                        );
                    }
                }
        );
    }

    public void sendNotifications() {

    }
}
