package org.devgateway.toolkit.persistence.dao;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;

/**
 * @author idobre
 * @since 6/22/16
 */
@Entity
@Audited
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AdminSettings extends AbstractAuditableEntity {
    private static final long serialVersionUID = -1051140524022133178L;

    private Integer excelBatchSize;

    private Boolean rebootServer = false;

    private String adminEmail = null;

    private Boolean enableDailyAutomatedImport = false;

    private String importFilesPath = null;

    /**
     * This disables the security of /api/ endpoints, should be used for demo purposes only
     */
    private Boolean disableApiSecurity = false;

    private Integer autosaveTime;

    private Boolean emailNotification = false;

    @Override
    public AbstractAuditableEntity getParent() {
        return null;
    }

    public Integer getExcelBatchSize() {
        return excelBatchSize;
    }

    public void setExcelBatchSize(final Integer excelBatchSize) {
        this.excelBatchSize = excelBatchSize;
    }

    public Boolean getRebootServer() {
        return rebootServer;
    }

    public void setRebootServer(final Boolean rebootServer) {
        this.rebootServer = rebootServer;
    }

    public Integer getAutosaveTime() {
        return autosaveTime;
    }

    public void setAutosaveTime(final Integer autosaveTime) {
        this.autosaveTime = autosaveTime;
    }

    public Boolean getEmailNotification() {
        return emailNotification;
    }

    public void setEmailNotification(final Boolean emailNotification) {
        this.emailNotification = emailNotification;
    }

    public Boolean getDisableApiSecurity() {
        return disableApiSecurity;
    }

    public void setDisableApiSecurity(Boolean disableApiSecurity) {
        this.disableApiSecurity = disableApiSecurity;
    }


    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public Boolean getEnableDailyAutomatedImport() {
        return enableDailyAutomatedImport;
    }

    public void setEnableDailyAutomatedImport(Boolean enableDailyAutomatedImport) {
        this.enableDailyAutomatedImport = enableDailyAutomatedImport;
    }

    public String getImportFilesPath() {
        return importFilesPath;
    }

    public void setImportFilesPath(String importFilesPath) {
        this.importFilesPath = importFilesPath;
    }
}
