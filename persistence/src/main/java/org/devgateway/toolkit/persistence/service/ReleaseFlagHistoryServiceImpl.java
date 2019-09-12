package org.devgateway.toolkit.persistence.service;

import org.devgateway.toolkit.persistence.dao.flags.ReleaseFlagHistory;
import org.devgateway.toolkit.persistence.repository.ReleaseFlagHistoryRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author mpostelnicu
 */
@Service
public class ReleaseFlagHistoryServiceImpl extends BaseJpaServiceImpl<ReleaseFlagHistory>
        implements ReleaseFlagHistoryService {

    @Autowired
    private ReleaseFlagHistoryRepository releaseFlagHistoryRepository;


    @Override
    protected BaseJpaRepository<ReleaseFlagHistory, Long> repository() {
        return releaseFlagHistoryRepository;
    }

    @Override
    public ReleaseFlagHistory newInstance() {
        return new ReleaseFlagHistory();
    }

    @Override
    public Optional<ReleaseFlagHistory> findLatestReleaseFlagHistory(String releaseId) {
        return releaseFlagHistoryRepository.findFirstByReleaseIdOrderByFlaggedDateDesc(releaseId);
    }
}
