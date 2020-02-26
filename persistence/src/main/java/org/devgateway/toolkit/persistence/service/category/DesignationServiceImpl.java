/**
 *
 */
package org.devgateway.toolkit.persistence.service.category;

import org.devgateway.toolkit.persistence.dao.categories.Designation;
import org.devgateway.toolkit.persistence.repository.category.DesignationRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.devgateway.toolkit.persistence.repository.norepository.TextSearchableRepository;
import org.devgateway.toolkit.persistence.service.BaseJpaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author mpostelnicu
 *
 */
@Service
@Transactional
public class DesignationServiceImpl extends BaseJpaServiceImpl<Designation> implements DesignationService {

    @Autowired
    private DesignationRepository repository;

    @Override
    protected BaseJpaRepository<Designation, Long> repository() {
        return repository;
    }

    @Override
    public TextSearchableRepository<Designation, Long> textRepository() {
        return repository;
    }

    @Override
    public Designation newInstance() {
        return new Designation();
    }
}
