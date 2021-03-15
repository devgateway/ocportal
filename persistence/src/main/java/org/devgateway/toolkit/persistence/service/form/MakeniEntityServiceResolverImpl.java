package org.devgateway.toolkit.persistence.service.form;


import static java.util.stream.Collectors.toList;

import com.google.common.collect.ImmutableMap;
import org.devgateway.toolkit.persistence.dao.Person;
import org.devgateway.toolkit.persistence.dao.form.AbstractMakueniEntity;
import org.devgateway.toolkit.persistence.dao.form.AbstractTenderProcessMakueniEntity;
import org.devgateway.toolkit.persistence.dao.form.CabinetPaper;
import org.devgateway.toolkit.persistence.dao.form.Lockable;
import org.devgateway.toolkit.persistence.dao.form.ProcurementPlan;
import org.devgateway.toolkit.persistence.dao.form.Project;
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

@Service
@Transactional
public class MakeniEntityServiceResolverImpl implements MakueniEntityServiceResolver {

    @Autowired
    private AdminSettingsService adminSettingsService;

    @Autowired
    private TenderProcessEntityServiceResolver tenderProcessEntityServiceResolver;

    @Autowired
    private ProcurementPlanService procurementPlanService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private CabinetPaperService cabinetPaperService;

    private Map<Class<?>, AbstractMakueniEntityService<?>> serviceMap;

    @PostConstruct
    public void init() {
        serviceMap = ImmutableMap.<Class<?>, AbstractMakueniEntityService<?>>builder()
                .put(Project.class, projectService)
                .put(ProcurementPlan.class, procurementPlanService)
                .put(CabinetPaper.class, cabinetPaperService)
                .build();
    }

    public <S extends AbstractMakueniEntity> S saveAndFlushMakueniEntity(S entity) {
        Class<? extends AbstractMakueniEntity> aClass = entity.getClass();
        if (AbstractTenderProcessMakueniEntity.class.isAssignableFrom(aClass)) {
            return (S) tenderProcessEntityServiceResolver.saveAndFlush((AbstractTenderProcessMakueniEntity) entity);
        } else {
            AbstractMakueniEntityService service = serviceMap.get(aClass);
            return (S) service.saveAndFlush(entity);
        }
    }

    @Override
    public void unlock(Lockable entity) {
        AbstractMakueniEntityService<? extends AbstractMakueniEntity> service = serviceMap.get(entity.getClass());
        service.unlock(entity.getId());
    }

    @Override
    public List<? extends AbstractMakueniEntity> getAllLocked(Person person) {
        return serviceMap.values().stream()
                .flatMap(s -> s.getAllLocked(person).stream())
                .collect(toList());
    }

    @Override
    public List<? extends AbstractMakueniEntity> getAllLocked() {
        return serviceMap.values().stream()
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
