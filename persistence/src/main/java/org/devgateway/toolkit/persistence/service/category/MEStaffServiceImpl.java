/**
 *
 */
package org.devgateway.toolkit.persistence.service.category;

import org.devgateway.toolkit.persistence.dao.categories.MEStaff;
import org.devgateway.toolkit.persistence.repository.category.MEStaffRepository;
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
public class MEStaffServiceImpl extends BaseJpaServiceImpl<MEStaff> implements MEStaffService {

    @Autowired
    private MEStaffRepository repository;

    @Override
    protected BaseJpaRepository<MEStaff, Long> repository() {
        return repository;
    }

    @Override
    public TextSearchableRepository<MEStaff, Long> textRepository() {
        return repository;
    }

    @Override
    public MEStaff newInstance() {
        return new MEStaff();
    }
}
