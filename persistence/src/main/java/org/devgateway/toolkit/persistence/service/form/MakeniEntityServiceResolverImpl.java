package org.devgateway.toolkit.persistence.service.form;

import static java.util.stream.Collectors.toList;

import com.google.common.collect.ImmutableMap;
import org.devgateway.toolkit.persistence.dao.Person;
import org.devgateway.toolkit.persistence.dao.form.AbstractMakueniEntity;
import org.devgateway.toolkit.persistence.dao.form.Lockable;
import org.devgateway.toolkit.persistence.service.AdminSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class MakeniEntityServiceResolverImpl implements MakueniEntityServiceResolver {

    @Autowired
    private AdminSettingsService adminSettingsService;

    @Autowired
    private List<AbstractMakueniEntityService<?>> serviceList;

    private Map<Class<?>, AbstractMakueniEntityService<?>> serviceMap;

    @PostConstruct
    public void init() {
        serviceMap = ImmutableMap.copyOf(serviceList.stream()
                .collect(Collectors.toMap(s -> s.newInstance().getClass(), s -> s)));
    }

    @Override
    public void unlock(Lockable entity) {
        serviceMap.get(entity.getClass())
                .unlock(entity.getId());
    }

    @Override
    public List<? extends AbstractMakueniEntity> getAllLocked(Person person) {
        return serviceList.stream()
                .flatMap(s -> s.getAllLocked(person).stream())
                .collect(toList());
    }

    @Override
    public List<? extends AbstractMakueniEntity> getAllLocked() {
        return serviceList.stream()
                .flatMap(s -> s.getAllLocked().stream())
                .collect(toList());
    }

    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void releaseLocks() {
        Integer unlockAfterHours = adminSettingsService.getSettings().getUnlockAfterHours();
        ZonedDateTime lockedAt = ZonedDateTime.now().minus(Duration.ofHours(unlockAfterHours));
        getAllLocked().stream()
                .filter(e -> e.getLastModifiedDate().isPresent()
                        && e.getLastModifiedDate().get().isBefore(lockedAt))
                .forEach(e -> e.setOwner(null));
    }
}
