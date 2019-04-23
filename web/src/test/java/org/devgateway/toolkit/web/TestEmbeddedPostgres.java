package org.devgateway.toolkit.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import ru.yandex.qatools.embed.postgresql.EmbeddedPostgres;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import static java.util.Arrays.asList;
import static ru.yandex.qatools.embed.postgresql.EmbeddedPostgres.cachedRuntimeConfig;
import static ru.yandex.qatools.embed.postgresql.distribution.Version.Main.PRODUCTION;

/**
 * @author idobre
 * @since 7/3/19
 * <p>
 * Class that starts and embedded postgresql db server
 */
@Configuration
@Profile("integration")
@PropertySource("classpath:/org/devgateway/toolkit/persistence/application.properties")
public class TestEmbeddedPostgres {
    private static final Logger logger = LoggerFactory.getLogger(TestEmbeddedPostgres.class);

    private static final int DB_PORT = 31219;

    private static final List<String> DEFAULT_ADD_PARAMS = asList(
            "-E", "SQL_ASCII",
            "--locale=C",
            "--lc-collate=C",
            "--lc-ctype=C");

    @Value("${spring.datasource.username}")
    private String springDatasourceUsername;

    @Value("${spring.datasource.password}")
    private String springDatasourcePassword;

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    @DependsOn(value = {"postgresServer"})
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * Graciously starts a Postgres Database Server when the tests start up
     *
     * @return
     * @throws IOException
     */
    @Bean(destroyMethod = "stop")
    public EmbeddedPostgres postgresServer() throws IOException {
        // starting Postgres
        final EmbeddedPostgres postgres = new EmbeddedPostgres(PRODUCTION);
        final String url = postgres.start(cachedRuntimeConfig(
                Paths.get(System.getProperty("user.home") + "/.embedpostgresql")), "localhost",
                DB_PORT, "makueni-test", springDatasourceUsername, springDatasourcePassword, DEFAULT_ADD_PARAMS);

        logger.debug("Embedded postgres url: " + url);

        return postgres;
    }
}
