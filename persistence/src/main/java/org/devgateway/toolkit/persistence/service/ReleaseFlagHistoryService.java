package org.devgateway.toolkit.persistence.service;

import org.devgateway.toolkit.persistence.dao.flags.ReleaseFlagHistory;

import java.util.Optional;

public interface ReleaseFlagHistoryService extends BaseJpaService<ReleaseFlagHistory> {

    Optional<ReleaseFlagHistory> findLatestReleaseFlagHistory(String releaseId);
}
