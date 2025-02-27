package org.devgateway.ocds.persistence.mongo.spring;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongoCmdOptions;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import org.devgateway.toolkit.persistence.mongo.spring.MongoTemplateConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.Collections;

/**
 * Created by mpostelnicu on 6/12/17.
 */
@Configuration
@Profile("integration")
public class MongoTemplateTestConfig {

    @Autowired
    private MongoProperties properties;

    @Autowired
    private MongoCustomConversions customConversions;

    @Autowired
    private Environment environment;

    @Autowired(required = false)
    private MongoClientSettings options;

    private String originalUri;

    @Bean(destroyMethod = "stop")
    public MongodProcess mongodProcess(MongodExecutable embeddedMongoServer) throws IOException {
        return embeddedMongoServer.start();
    }

    @Bean(destroyMethod = "stop")
    public MongodExecutable embeddedMongoServer(MongodStarter mongodStarter, MongodConfig iMongodConfig)
            throws IOException {
        return mongodStarter.prepare(iMongodConfig);
    }

    @Bean
    public MongodConfig mongodConfig() {
        return  MongodConfig.builder().version(Version.V4_2_22)
                .cmdOptions(MongoCmdOptions.builder().useNoJournal(true)
                        .build())
                .build();
    }

    @Bean
    public MongodStarter mongodStarter() {
        return MongodStarter.getDefaultInstance();
    }

    @Bean
    public MongoClient mongoClient() {
        ConnectionString connectionString = new ConnectionString(properties.getUri());

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();

        return MongoClients.create(settings);
    }


    @Bean(name = "mongoTemplate")
    public MongoTemplate mongoTemplate(MongodProcess mongodProcess) throws Exception {
        Net net = mongodProcess.getConfig().net();
        properties.setHost(net.getServerAddress().getHostName());
        properties.setPort(net.getPort());
        properties.setDatabase(originalUri);
        properties.setUri(null);

        // Create the MongoClient using the properties
        ConnectionString connectionString = new ConnectionString(properties.getUri());
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        MongoClient mongoClient = MongoClients.create(mongoClientSettings);

        MongoDatabaseFactory mongoDatabaseFactory = new SimpleMongoClientDatabaseFactory(mongoClient, properties.getDatabase());
        MongoTemplate template = new MongoTemplate(mongoDatabaseFactory);
        ((MappingMongoConverter) template.getConverter()).setCustomConversions(customConversions);
        return template;
    }

    @PostConstruct
    public void postConstruct() {
        //set uri string
        originalUri = new ConnectionString(properties.getUri()).getDatabase();
    }

    /**
     * Creates a shadow template configuration by adding "-shadow" as postfix of database name.
     * This is used to replicate the entire database structure in a shadow/temporary database location
     *
     * @return
     * @throws Exception
     */
    @Bean(name = "shadowMongoTemplate")
    public MongoTemplate shadowMongoTemplate(MongodProcess mongodProcess) throws Exception {
        Net net = mongodProcess.getConfig().net();
        properties.setHost(net.getServerAddress().getHostName());
        properties.setPort(net.getPort());
        properties.setDatabase(originalUri + MongoTemplateConfig.SHADOW_POSTFIX);
        properties.setUri(null);

        // Create the MongoClient using the properties
        ConnectionString connectionString = new ConnectionString(properties.getUri());
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        MongoClient mongoClient = MongoClients.create(mongoClientSettings);

        MongoDatabaseFactory mongoDatabaseFactory = new SimpleMongoClientDatabaseFactory(mongoClient, properties.getDatabase());
        MongoTemplate template = new MongoTemplate(mongoDatabaseFactory);
        ((MappingMongoConverter) template.getConverter()).setCustomConversions(customConversions);
        return template;
    }
}
