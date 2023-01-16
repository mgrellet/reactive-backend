package com.reactive.config;

import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.SimpleReactiveMongoDatabaseFactory;

@Profile(value="local")
@Configuration
public class DataConfig {
    public static final String DATABASE_NAME = "reservations";

    public @Bean ReactiveMongoDatabaseFactory mongoDatabaseFactory(){
        return new SimpleReactiveMongoDatabaseFactory(
                MongoClients.create("mongodb://root:1234@localhost"), DATABASE_NAME);

    }
    public @Bean ReactiveMongoTemplate reactiveMongoTemplate() {
        return new ReactiveMongoTemplate(mongoDatabaseFactory());
    }

}
