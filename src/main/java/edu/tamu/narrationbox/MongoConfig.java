package edu.tamu.narrationbox;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories()
public class MongoConfig extends AbstractMongoConfiguration {
    @Value("${spring.data.mongodb.port}")
    private String port;

    @Value("${spring.data.mongodb.host}")
    private String hostName;

    @Value("${spring.data.mongodb.database}")
    private String database;

    @Value("${spring.data.mongodb.username}")
    private String username;

    @Value("${spring.data.mongodb.password}")
    private String password;

    @Override
    protected String getDatabaseName() {
        return database;
    }

    @Bean(name = "mongoClient")
    @Override
    public MongoClient mongoClient() {
            String connectionStringURI = String.format("mongodb://%s:%s@%s:%s/%s", username, password, hostName, port, database);
            return new MongoClient(new MongoClientURI(connectionStringURI));
    }
}