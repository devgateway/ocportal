package org.devgateway.toolkit.persistence.service.category;

import org.devgateway.toolkit.persistence.dao.categories.ContractDocument;
import org.devgateway.toolkit.persistence.repository.category.ContractDocumentRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.devgateway.toolkit.persistence.repository.norepository.TextSearchableRepository;
import org.devgateway.toolkit.persistence.service.BaseJpaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author gmutuhu
 *
 */
@Service
@CacheConfig(cacheNames = "servicesCache")
@Transactional(readOnly = true)
public class ContractDocumentServiceImpl extends BaseJpaServiceImpl<ContractDocument>
        implements ContractDocumentService {

    @Autowired
    private ContractDocumentRepository repository;

    @Override
    public ContractDocument newInstance() {
        return new ContractDocument();
    }

    @Override
    public TextSearchableRepository<ContractDocument, Long> textRepository() {
        return repository;
    }

    @Override
    protected BaseJpaRepository<ContractDocument, Long> repository() {
        return repository;
    }
}
