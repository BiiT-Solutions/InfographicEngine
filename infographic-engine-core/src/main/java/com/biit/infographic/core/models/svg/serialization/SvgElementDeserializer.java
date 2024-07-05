package com.biit.infographic.core.models.svg.serialization;

import com.biit.infographic.core.models.svg.ElementType;
import com.biit.infographic.core.models.svg.SvgElement;
import com.biit.infographic.core.models.svg.exceptions.InvalidAttributeException;
import com.biit.infographic.logger.InfographicEngineLogger;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public abstract class SvgElementDeserializer<T extends SvgElement> extends StdDeserializer<T> {


    private final Class<T> specificClass;

    public SvgElementDeserializer(Class<T> aClass) {
        super(aClass);
        this.specificClass = aClass;
    }

    public void deserialize(T element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        element.setId(DeserializerParser.parseString("id", jsonObject));
    }


    @Override
    public T deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        final JsonNode jsonObject = jsonParser.getCodec().readTree(jsonParser);

        try {
            final ElementType type = ElementType.fromString(jsonObject.get("elementType").asText());
            if (type == null) {
                return null;
            }

            final T element = (T) type.getRelatedClass().getDeclaredConstructor().newInstance();
            deserialize(element, jsonObject, deserializationContext);
            return element;
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException
                 | NullPointerException e) {
            InfographicEngineLogger.severe(this.getClass(), "Invalid node:\n" + jsonObject.toPrettyString());
            InfographicEngineLogger.errorMessage(this.getClass(), e);
            throw new InvalidAttributeException(this.getClass(), e);
        }
    }
}
