package com.biit.knowledge;

import com.biit.infographic.texts.InfographicTranslationTextClient;
import com.biit.ks.client.TestTextClient;
import com.biit.ks.models.ITextClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.TestPropertySource;

@Controller
@TestPropertySource("classpath:application.properties")
@ComponentScan(basePackages = {"com.biit.infographic", "com.biit.usermanager.client", "com.biit.server.client", "com.biit.kafka", "com.biit.appointment.rest.client",
        "com.biit.ks.client", "com.biit.factmanager.client", "com.biit.cadt.texts"}, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = {InfographicTranslationTextClient.class})})
public class KnowledgeTextConfig {


    public KnowledgeTextConfig() {

    }


    @Bean
    public ITextClient textClient() {
        return new TestTextClient();
    }
}
