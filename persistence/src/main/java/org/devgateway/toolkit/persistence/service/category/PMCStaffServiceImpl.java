/**
 *
 */
package org.devgateway.toolkit.persistence.service.category;

import org.devgateway.toolkit.persistence.dao.categories.PMCStaff;
import org.devgateway.toolkit.persistence.repository.category.PMCStaffRepository;
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
public class PMCStaffServiceImpl extends BaseJpaServiceImpl<PMCStaff> implements PMCStaffService {

    @Autowired
    private PMCStaffRepository repository;

    @Override
    protected BaseJpaRepository<PMCStaff, Long> repository() {
        return repository;
    }

    @Override
    public TextSearchableRepository<PMCStaff, Long> textRepository() {
        return repository;
    }

    @Override
    public PMCStaff newInstance() {
        return new PMCStaff();
    }
}
