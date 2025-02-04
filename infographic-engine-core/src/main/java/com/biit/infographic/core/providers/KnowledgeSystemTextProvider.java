package com.biit.infographic.core.providers;

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
