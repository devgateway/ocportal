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

import org.devgateway.toolkit.persistence.dao.categories.FiscalYear;
import org.devgateway.toolkit.persistence.repository.norepository.TextSearchableRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author mpostelnicu
 */
@Transactional
public interface FiscalYearRepository extends TextSearchableRepository<FiscalYear, Long> {

    @Override
    @Query("select cat from  #{#entityName} cat where lower(cat.name) like %:name%")
    Page<FiscalYear> searchText(@Param("name") String name, Pageable page);

    @Query("select distinct p.fiscalYear from ProcurementPlan p order by p.fiscalYear.startDate desc")
    List<FiscalYear> getYearsWithData();
}
