package org.devgateway.ocds.web.util;

import org.devgateway.toolkit.persistence.dao.AdminSettings;
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

    @Value("${googleAnalyticsTrackingId:#{null}}")
    private String googleAnalyticsTrackingId;

    public String getGoogleAnalyticsTrackingId() {
        return googleAnalyticsTrackingId;
    }

    public int getAutosaveTime() {
        Integer autosaveTime = getSettings().getAutosaveTime();
        if (ObjectUtils.isEmpty(autosaveTime)) {
            return AUTOSAVE_TIME_DEFAULT;
        }
        return autosaveTime;
    }

    public boolean getRebootServer() {
        Boolean rebootServer = getSettings().getRebootServer();
        if (rebootServer == null) {
            return false;
        }
        return rebootServer;
    }


    public static final String DEFAULT_LANGUAGE = "en_US";

    private static final Integer EXCELBATCHSIZEDEFAULT = 5000;

    private static final Integer DAYS_SUBMITTED_REMINDER_DEFAULT = 14;

    public AdminSettings getSettings() {
        return adminSettingsService.getSettings();
    }

    public Integer getExcelBatchSize() {
        AdminSettings adminSettings = getSettings();
        if (adminSettings.getExcelBatchSize() == null) {
            return EXCELBATCHSIZEDEFAULT;
        }
        return adminSettings.getExcelBatchSize();
    }

    public Integer getDaysSubmittedReminder() {
        AdminSettings adminSettings = getSettings();
        if (adminSettings.getDaysSubmittedReminder() == null) {
            return DAYS_SUBMITTED_REMINDER_DEFAULT;
        }
        return adminSettings.getDaysSubmittedReminder();
    }

}
