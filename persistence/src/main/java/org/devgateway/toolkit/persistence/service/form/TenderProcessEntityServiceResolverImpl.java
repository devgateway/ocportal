package org.devgateway.toolkit.persistence.service.form;

import com.google.common.collect.ImmutableMap;
import org.devgateway.toolkit.persistence.dao.form.AbstractTenderProcessMakueniEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Octavian Ciubotaru
 */
@Service
@Transactional
public class TenderProcessEntityServiceResolverImpl implements TenderProcessEntityServiceResolver {

    private Map<Class<?>, AbstractTenderProcessEntityService<?>> serviceMap;

    @Autowired
    private List<AbstractTenderProcessEntityService<?>> tenderProcessServiceList;

    @PostConstruct
    public void init() {
       serviceMap = ImmutableMap.copyOf(tenderProcessServiceList.stream()
               .collect(Collectors.toMap(s -> s.newInstance().getClass(), s -> s)));
    }

    @Override
    public <T extends AbstractTenderProcessMakueniEntity> long countByTenderProcess(T entity) {
        return getService(entity).countByTenderProcess(entity.getId(), entity.getTenderProcess());
    }

    @Override
    public <T extends AbstractTenderProcessMakueniEntity> T saveAndFlush(T entity) {
        return (T) getService(entity).saveAndFlush(entity);
    }

    private <T extends AbstractTenderProcessMakueniEntity> AbstractTenderProcessEntityService getService(T entity) {
        Class<? extends AbstractTenderProcessMakueniEntity> entityClass = entity.getClass();
        AbstractTenderProcessEntityService<?> service = serviceMap.get(entityClass);
        if (service == null) {
            throw new RuntimeException("No service configured for " + entityClass);
        }
        return service;
    }
}
