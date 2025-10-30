package com.biit.cadt;

/*-
 * #%L
 * Infographic Engine v2 (Core)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.biit.infographic.texts.InfographicTranslationTextClient;
import com.biit.ks.client.TestTextClient;
import com.biit.ks.models.ITextClient;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.TestPropertySource;

@Controller
@TestPropertySource("classpath:application.properties")
@ComponentScan(basePackages = {"com.biit.infographic", "com.biit.usermanager.client", "com.biit.server.client", "com.biit.kafka", "com.biit.appointment.rest.client",
        "com.biit.ks.client", "com.biit.factmanager.client", "com.biit.cadt.texts"}, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = {TestTextClient.class})})
public class InfographicTranslationTextConfig {

    private final MessageSource messageSource;

    public InfographicTranslationTextConfig(MessageSource messageSource) {
        this.messageSource = messageSource;
    }


    @Bean
    public ITextClient textClient() {
        return new InfographicTranslationTextClient(messageSource);
    }
}
