package org.devgateway.toolkit.persistence.service.prequalification;

import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationSchema;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationSchemaItem;
import org.devgateway.toolkit.persistence.dao.prequalification.PrequalificationSchema_;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.devgateway.toolkit.persistence.repository.prequalification.PrequalificationSchemaRepository;
import org.devgateway.toolkit.persistence.service.BaseJpaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.metamodel.SingularAttribute;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
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

    @Override
    public long countByNameOrPrefix(PrequalificationSchema schema) {
        return prequalificationSchemaRepository.countByNameOrPrefix(schema.getId(),
                schema.getName(), schema.getPrefix());
    }

    @Override
    public Long saveDuplicateSchema(Long schemaId) {
        Optional<PrequalificationSchema> byId = findById(schemaId);
        if (!byId.isPresent()) {
            throw new RuntimeException("Cannot find prequalification schema with id " + schemaId);
        }
        PrequalificationSchema existingSchema = byId.get();

        PrequalificationSchema schemaCopy = newInstance();
        schemaCopy.setName("Copy of " + existingSchema.getName());
        schemaCopy.setPrefix(existingSchema.getPrefix());
        existingSchema.getItems().stream().map(i -> {
            PrequalificationSchemaItem item = new PrequalificationSchemaItem();
            item.setName(i.getName());
            item.setCode(i.getCode());
            item.setParent(schemaCopy);
            item.getCompanyCategories().addAll(i.getCompanyCategories());
            return item;
        }).collect(Collectors.toCollection(schemaCopy::getItems));
        schemaCopy.setStatus(DBConstants.Status.DRAFT);

        return save(schemaCopy).getId();
    }
}