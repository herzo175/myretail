package com.herzo175.myretail.configuration;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Value("${mongodb.address}")
    private String mongoDBAddress;

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create(mongoDBAddress);
    }
}
