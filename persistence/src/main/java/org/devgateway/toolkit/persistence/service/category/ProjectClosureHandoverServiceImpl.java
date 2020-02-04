/**
 *
 */
package org.devgateway.toolkit.persistence.service.category;

import org.devgateway.toolkit.persistence.dao.categories.ProjectClosureHandover;
import org.devgateway.toolkit.persistence.repository.category.ProjectClosureHandoverRepository;
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
public class ProjectClosureHandoverServiceImpl extends BaseJpaServiceImpl<ProjectClosureHandover> implements
        ProjectClosureHandoverService {

    @Autowired
    private ProjectClosureHandoverRepository repository;

    @Override
    protected BaseJpaRepository<ProjectClosureHandover, Long> repository() {
        return repository;
    }

    @Override
    public TextSearchableRepository<ProjectClosureHandover, Long> textRepository() {
        return repository;
    }

    @Override
    public ProjectClosureHandover newInstance() {
        return new ProjectClosureHandover();
    }
}
