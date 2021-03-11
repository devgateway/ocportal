package org.devgateway.toolkit.persistence.service.category;

import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.repository.category.DepartmentRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author mpostelnicu
 */
@Service
@Transactional(readOnly = true)
public class DepartmentServiceImpl extends CategoryServiceImpl<Department> implements DepartmentService {
    @Autowired
    private DepartmentRepository repository;
    
    @Override
    protected BaseJpaRepository<Department, Long> repository() {
        return repository;
    }

    @Override
    public Department newInstance() {
        return new Department();
    }


}
