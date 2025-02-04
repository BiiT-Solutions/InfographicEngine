package com.biit.infographic.core;

import com.biit.infographic.texts.InfographicTranslationTextClient;
import com.biit.ks.client.TestTextClient;
import com.biit.ks.models.ITextClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;

@Configuration
@TestPropertySource("classpath:application.properties")
@ComponentScan({"com.biit.infographic", "com.biit.usermanager.client", "com.biit.server.client", "com.biit.kafka", "com.biit.appointment.rest.client",
        "com.biit.factmanager.client"})
public class TestConfiguration {
}
