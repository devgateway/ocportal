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
package org.devgateway.toolkit.forms.service;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.zeroturnaround.zip.ZipUtil;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author mpostelnicu Provides built-in backup services. Defaults to the
 *         database location derby.system.home. Currently works only for Derby.
 *         Runs 9PM daily (good backup time for both EST and CET)
 */
@Service
public class DerbyDatabaseBackupService {

    private static final Logger logger = LoggerFactory.getLogger(DerbyDatabaseBackupService.class);

    public static final String DATABASE_PRODUCT_NAME_APACHE_DERBY = "Apache Derby";
    public static final String ARCHIVE_SUFFIX = ".zip";

    @Autowired
    private DataSource datasource;

    private String lastBackupURL;

    private String databaseName = "sample";

    /**
     * Invokes backup database. This is invoked by Spring {@link Scheduled} We
     * use a cron format and invoke it every day at 21:00 server time. That
     * should be a good time for backup for both EST and CET
     */
    @Scheduled(cron = "0 0 21 * * ?")
    public void backupDatabase() {
        String databaseProductName;

        Connection conn = null;
        try {
            conn = datasource.getConnection();
            databaseProductName = conn.getMetaData().getDatabaseProductName();
        } catch (SQLException e) {
            logger.error("Cannot read databaseProductName from Connection!"
                    + DerbyDatabaseBackupService.class.getCanonicalName() + " cannot continue!" + e);
            return;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        if (DATABASE_PRODUCT_NAME_APACHE_DERBY.equals(databaseProductName)) {
            backupDerbyDatabase();
        } else {
            throw new RuntimeException(
                    "Scheduled database backup for unsupported database type " + databaseProductName);
        }
    }

    /**
     * Gets the URL (directory/file) of the backupPath. Adds as prefixes the
     * last leaf of backup's location parent directory + {@link #databaseName}
     * If the backupPath does not have a parent, it uses the host name from
     * {@link InetAddress#getLocalHost()}
     *
     * @param backupPath
     *            the parent directory for the backup
     * @return the backup url to be used by the backup procedure
     * @throws UnknownHostException
     */
    private String createBackupURL(final String backupPath) {
        java.text.SimpleDateFormat todaysDate = new java.text.SimpleDateFormat("yyyyMMdd-HHmmss");
        String parent = null;
        Path originalPath = Paths.get(backupPath);
        Path filePath = originalPath.getFileName();
        if (filePath != null) {
            parent = filePath.toString();
        } else {
            try {
                // fall back to hostname instead
                parent = InetAddress.getLocalHost().getHostName();
            } catch (UnknownHostException e) {
                logger.debug("Cannot get localhost/hostname! " + e);
                return null;
            }
        }

        String backupURL = backupPath + "/" + parent + "-" + databaseName + "-"
                + todaysDate.format((java.util.Calendar.getInstance()).getTime());
        return backupURL;
    }

    /**
     * Use backup.home system variable, if exists, as homedir for backups If
     * backup.home does not exist try using derby.system.home If that is also
     * null, use user.dir
     *
     * @return the backupURL
     */
    private String createBackupURL() {
        String backupHomeString = System.getProperty("backup.home");
        if (backupHomeString == null) {
            backupHomeString = System.getProperty("derby.system.home") != null ? System.getProperty("derby.system.home")
                    : System.getProperty("user.dir");
        }

        String backupURL = createBackupURL(backupHomeString);
        return backupURL;
    }

    /**
     * Backup the On-Line Derby database. This temporarily locks the db in
     * readonly mode
     *
     * Invokes SYSCS_BACKUP_DATABASE and dumps the database to the temporary
     * directory Use {@link ZipUtil#pack(File, File)} to zip the directory
     * Deletes the temporary directory
     *
     * @see #createBackupURL(String)
     */
    private void backupDerbyDatabase() {

        lastBackupURL = createBackupURL();
