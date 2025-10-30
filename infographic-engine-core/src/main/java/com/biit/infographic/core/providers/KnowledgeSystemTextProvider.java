package com.biit.infographic.core.providers;

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

import com.biit.ks.dto.exceptions.TextNotFoundException;
import com.biit.ks.models.IKnowledgeSystemTextProvider;
import com.biit.ks.models.ITextClient;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class KnowledgeSystemTextProvider implements IKnowledgeSystemTextProvider {

    private final ITextClient textClient;

    public KnowledgeSystemTextProvider(ITextClient textClient) {
        this.textClient = textClient;
    }

    @Override
    public String get(String textName, Locale language) throws TextNotFoundException {
        if (textName != null && language != null) {
            return textClient.get(textName, language).orElseThrow(()
                    -> new TextNotFoundException(this.getClass(), "Text not found with name '" + textName + "' for locale '" + language + "'."));
        }
        return "";
    }
}
