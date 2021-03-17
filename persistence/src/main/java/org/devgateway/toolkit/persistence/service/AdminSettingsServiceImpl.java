package org.devgateway.toolkit.persistence.service;

import java.util.List;

import org.devgateway.toolkit.persistence.dao.AdminSettings;
import org.devgateway.toolkit.persistence.repository.AdminSettingsRepository;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author idobre
 * @since 2019-03-04
 */
@Service
@Transactional(readOnly = true)
public class AdminSettingsServiceImpl extends BaseJpaServiceImpl<AdminSettings> implements AdminSettingsService {
    @Autowired
    private AdminSettingsRepository adminSettingsRepository;

    @Override
    protected BaseJpaRepository<AdminSettings, Long> repository() {
        return adminSettingsRepository;
    }

    @Override
    public AdminSettings newInstance() {
        return new AdminSettings();
    }

    @Override
    public AdminSettings getSettings() {
        List<AdminSettings> list = adminSettingsRepository.findAll();
        if (list.size() == 0) {
            return new AdminSettings();
        } else {
            return list.get(0);
        }
    }
}
