package com.biit.infographic.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ConfigurationPropertiesScan({"com.biit.infographic"})
@EnableJpaRepositories({"com.biit.infographic.persistence.repositories"})
@EntityScan({"com.biit.infographic.persistence.entities"})
@ComponentScan({"com.biit.infographic", "com.biit.usermanager.client", "com.biit.server.client", "com.biit.kafka", "com.biit.appointment.rest.client",
        "com.biit.ks.client", "com.biit.factmanager.client", "com.biit.cadt.texts"})
public class Server {
    public static void main(String[] args) {
        SpringApplication.run(Server.class, args);
    }
}
