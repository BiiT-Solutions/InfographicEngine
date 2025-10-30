package com.biit.infographic.core.engine.content;

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


import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.infographic.core.engine.Parameter;
import com.biit.infographic.core.providers.KnowledgeSystemTextProvider;
import com.biit.infographic.core.providers.UserProvider;
import com.biit.infographic.logger.InfographicEngineLogger;
import com.biit.ks.dto.exceptions.TextNotFoundException;
import com.biit.server.security.model.IAuthenticatedUser;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

@Component
public class KnowledgeSystemContent {

    private static final String KNOWLEDGE_SYTEM_TAG = "$KnowledgeSystem{";

    private final KnowledgeSystemTextProvider knowledgeSystemTextProvider;

    private final UserProvider userProvider;

    public KnowledgeSystemContent(KnowledgeSystemTextProvider knowledgeSystemTextProvider, UserProvider userProvider) {
        this.knowledgeSystemTextProvider = knowledgeSystemTextProvider;
        this.userProvider = userProvider;
    }


    public void setKnowledgeSystemValues(Set<Parameter> parameters, DroolsSubmittedForm droolsSubmittedForm, Locale locale) {
        if (parameters != null) {
            //Search a parameter with the knowledge system tag.
            for (Parameter parameter : parameters) {
                for (Map.Entry<String, String> attribute : parameter.getAttributes().entrySet()) {
                    try {
                        if (attribute.getValue().contains(KNOWLEDGE_SYTEM_TAG)) {
                            final String knowledgeSystemName = extractKnowledgeSystemItem(attribute.getValue());
                            if (locale == null) {
                                final IAuthenticatedUser user = userProvider.getUser(droolsSubmittedForm.getSubmittedBy());
                                locale = user != null && user.getLocale() != null ? user.getLocale() : Locale.ENGLISH;
                            }
                            final String translatedText = knowledgeSystemTextProvider.get(knowledgeSystemName, locale);
                            //Replace knowledgeSystem tag with obtained text.
                            attribute.setValue(attribute.getValue().replaceAll(Pattern.quote(KNOWLEDGE_SYTEM_TAG) + "(.*?)}", translatedText));
                        }
                    } catch (TextNotFoundException e) {
                        InfographicEngineLogger.errorMessage(this.getClass(), e);
                    }
                }
            }
        }
    }

    private String extractKnowledgeSystemItem(String value) {
        if (value == null || value.isEmpty()) {
            return "";
        }
        final String firstMatch = value.substring(value.indexOf(KNOWLEDGE_SYTEM_TAG) + KNOWLEDGE_SYTEM_TAG.length());
        return firstMatch.substring(0, firstMatch.indexOf('}'));
    }
}
