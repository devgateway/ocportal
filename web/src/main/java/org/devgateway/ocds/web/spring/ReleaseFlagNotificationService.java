package org.devgateway.ocds.web.spring;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ReleaseFlagNotificationService {

    private Map<Long, Map<String, Set<String>>> departmentFlagRelease;


    @PostConstruct
    public void init() {
        departmentFlagRelease = new ConcurrentHashMap<>();
    }


    private void addDepartmentFlagRelease(Long departmentId, String flag, String releaseId) {
        departmentFlagRelease.putIfAbsent(departmentId, new ConcurrentHashMap<>()).
                putIfAbsent(flag, ConcurrentHashMap.newKeySet()).add(releaseId);

    }

}
