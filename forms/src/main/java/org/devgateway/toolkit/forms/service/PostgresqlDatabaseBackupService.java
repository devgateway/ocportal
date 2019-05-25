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
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.zip.Deflater;

/**
 * @author mpostelnicu
 * @author idobre
 *
 * Provides built-in backup services. Defaults to the
 *         system property backup.home. Currently works only for PostgreSQL.
 *         Runs 9PM daily (good backup time for both EST and CET)
 */
@Service
public class PostgresqlDatabaseBackupService {
    private static final Logger logger = LoggerFactory.getLogger(PostgresqlDatabaseBackupService.class);

    private static final String DATABASE_PRODUCT_NAME_POSTGRESQL = "PostgreSQL";

    private static final String ARCHIVE_SUFFIX = ".zip";

    private static final String DATABASENAME = "makueni";

    @Autowired
    private DataSource datasource;

    /**
     * Invokes backup database. This is invoked by Spring {@link Scheduled} We
     * use a cron format and invoke it every day at 21:00 server time. That
     * should be a good time for backup for both EST and CET
     */
    // @Scheduled(cron = "0 0 21 * * ?")
    @Scheduled(cron = "0 0/1 * * * ?")
    public void backupDatabase() {
        final String databaseProductName;

        try {
            databaseProductName = datasource.getConnection().getMetaData().getDatabaseProductName();
        } catch (SQLException e) {
            logger.error("Cannot read databaseProductName from Connection!"
                    + PostgresqlDatabaseBackupService.class.getCanonicalName() + " cannot continue!" + e);
            return;
        }
        if (DATABASE_PRODUCT_NAME_POSTGRESQL.equals(databaseProductName)) {
            backupPostgreSQLDatabase();
        } else {
            throw new RuntimeException(
                    "Scheduled database backup for unsupported database type " + databaseProductName);
        }
    }

    /**
     * Gets the URL (directory/file) of the backupPath and adds as suffix {@link #DATABASENAME} at the end.
     *
     * @param backupPath
     *            the parent directory for the backup
     *
     * @return the backup url to be used by the backup procedure
     */
    private String createBackupURL(final String backupPath) {
        final SimpleDateFormat todaysDate = new SimpleDateFormat("yyyyMMdd-HHmmss");

        final String backupURL = backupPath + "/" + DATABASENAME + "-"
                + todaysDate.format((java.util.Calendar.getInstance()).getTime());
        return backupURL;
    }

    /**
     * Use backup.home system variable, if exists, as homedir for backups If
     * backup.home does not exist try using user.dir
     *
     * @return the backupURL
     */
    private String createBackupURL() {
        String backupHomeString = System.getProperty("backup.home");
        if (backupHomeString == null) {
            backupHomeString = System.getProperty("user.dir");
        }

        return createBackupURL(backupHomeString);
    }

    /**
     * Backup the PostgreSQL database using *pg_dump* command
     *
     * After the backup finishes use {@link ZipUtil#pack(File, File)} to zip the directory and
     * deletes the temporary directory
     *
     * @see #createBackupURL(String)
     */
    private void backupPostgreSQLDatabase() {
        final String lastBackupURL = createBackupURL();

        final ProcessBuilder processBuilder =
                new ProcessBuilder("/usr/bin/pg_dump",
                        "-U", "postgres",
                        "-Fd",
                        "makueni",
                        "-f", lastBackupURL);
        processBuilder.redirectErrorStream(true);
        try {
            final Process process = processBuilder.start();
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while (true) {
                line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                logger.info(line);
            }
        } catch (IOException e) {
            logger.error("Cannot perform database backup!", e);
        }

        final File backupURLFile = new File(lastBackupURL);
        // zip the contents and delete the dir
        ZipUtil.pack(backupURLFile, new File(lastBackupURL + ARCHIVE_SUFFIX), Deflater.BEST_COMPRESSION);
        // delete the backup directory that we just zipped
        try {
            FileUtils.deleteDirectory(backupURLFile);
        } catch (IOException e) {
            logger.error("Cannot delete temporary backup directory", e);
        }

        logger.info("Backed up database to " + lastBackupURL + ARCHIVE_SUFFIX);
    }
}
