package org.devgateway.toolkit.persistence.service.prequalification;

import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationSchema;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationSchema_;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.devgateway.toolkit.persistence.repository.prequalification.PrequalificationSchemaRepository;
import org.devgateway.toolkit.persistence.service.BaseJpaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.metamodel.SingularAttribute;

@Service
public class PrequalificationSchemaServiceImpl extends BaseJpaServiceImpl<PrequalificationSchema>
        implements PrequalificationSchemaService {

    @Autowired
    private PrequalificationSchemaRepository prequalificationSchemaRepository;

    @Override
    protected BaseJpaRepository<PrequalificationSchema, Long> repository() {
        return prequalificationSchemaRepository;
    }

    @Override
    public PrequalificationSchema newInstance() {
        return new PrequalificationSchema();
    }


    @Override
    public SingularAttribute<? super PrequalificationSchema, String> getTextAttribute() {
        return PrequalificationSchema_.name;
    }
}