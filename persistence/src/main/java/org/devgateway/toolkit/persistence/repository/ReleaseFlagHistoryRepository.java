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
package org.devgateway.toolkit.persistence.repository;

import org.devgateway.toolkit.persistence.dao.flags.ReleaseFlagHistory;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author mpostelnicu
 */
@Transactional
public interface ReleaseFlagHistoryRepository extends BaseJpaRepository<ReleaseFlagHistory, Long> {

    Optional<ReleaseFlagHistory> findFirstByReleaseIdOrderByFlaggedDateDesc(String releaseId);
}