package org.devgateway.ocds.web.util;

import java.util.List;
import org.devgateway.toolkit.persistence.dao.AdminSettings;
import org.devgateway.toolkit.persistence.repository.AdminSettingsRepository;
import org.devgateway.toolkit.persistence.service.AdminSettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * @author idobre
 * @since 6/22/16
 */
@Service
public class SettingsUtils {
    protected static Logger logger = LoggerFactory.getLogger(SettingsUtils.class);

    public static final int AUTOSAVE_TIME_DEFAULT = 10;

    @Autowired
    private AdminSettingsService adminSettingsService;

    private AdminSettings setting;

    @Value("${googleAnalyticsTrackingId:#{null}}")
    private String googleAnalyticsTrackingId;

    public String getGoogleAnalyticsTrackingId() {
        return googleAnalyticsTrackingId;
    }

    public int getAutosaveTime() {
        init();
        if (ObjectUtils.isEmpty(setting.getAutosaveTime())) {
            return AUTOSAVE_TIME_DEFAULT;
        }
        return setting.getAutosaveTime();
    }

    public boolean getRebootServer() {
        init();
        if (setting.getRebootServer() == null) {
            return false;
        }
        return setting.getRebootServer();
    }

    public AdminSettings getSetting() {
        init();
        return setting;
    }

    private void init() {
        final List<AdminSettings> list = adminSettingsService.findAll();
        if (list.size() == 0) {
            setting = new AdminSettings();
        } else {
            setting = list.get(0);
        }
    }


    public static final String DEFAULT_LANGUAGE = "en_US";

    private static final Integer EXCELBATCHSIZEDEFAULT = 5000;

    @Autowired
    private AdminSettingsRepository adminSettingsRepository;

    public AdminSettings getSettings() {
        List<AdminSettings> list = adminSettingsRepository.findAll();
        if (list.size() == 0) {
            return new AdminSettings();
        } else {
            return list.get(0);
        }
    }

    public Integer getExcelBatchSize() {
        AdminSettings adminSettings = getSettings();
        if (adminSettings.getExcelBatchSize() == null) {
            return EXCELBATCHSIZEDEFAULT;
        }
        return adminSettings.getExcelBatchSize();
    }


}
