/*******************************************************************************
 * Copyright (c) 2015 Development Gateway, Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License (MIT)
 * which accompanies this distribution, and is available at
 * https://opensource.org/licenses/MIT
 *
 * Contributors:
 * Development Gateway - initial API and implementation
 *******************************************************************************/
package org.devgateway.toolkit.persistence.repository.category;

import java.util.List;

import org.devgateway.toolkit.persistence.dao.categories.Department;
import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author mpostelnicu
 */
@Transactional
public interface DepartmentRepository extends CategoryRepository<Department> {
    
    @Query("select dept from  #{#entityName} dept inner join ProcurementPlan p on p.department.id = dept.id "
            + "where p.fiscalYear=:fiscalYear")
    List<Department> findDeptsInCurrentProcurementPlan(@Param("fiscalYear")FiscalYear fiscalYear);

}
