package com.biit.infographic.core.providers;

import com.biit.infographic.core.exceptions.TextNotFoundException;
import com.biit.ks.models.ITextClient;
import org.springframework.stereotype.Component;

@Component
public class KnowledgeSystemTextProvider {

    private final ITextClient textClient;

    public KnowledgeSystemTextProvider(ITextClient textClient) {
        this.textClient = textClient;
    }

    public String get(String textName, String language) throws TextNotFoundException {
        if (textName != null && language != null) {
            return textClient.get(textName, language).orElseThrow(()
                    -> new TextNotFoundException(this.getClass(), "Text not found with name '" + textName + "' for locale '" + language + "'."));
        }
        return "";
    }
}
