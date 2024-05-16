package com.biit.infographic.core.models.svg.serialization;

import com.biit.infographic.core.models.svg.ElementType;
import com.biit.infographic.core.models.svg.FillAttributes;
import com.biit.infographic.core.models.svg.exceptions.InvalidAttributeException;
import com.biit.infographic.logger.InfographicEngineLogger;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class FillAttributesDeserializer<T extends FillAttributes> extends StdDeserializer<T> {

    private final Class<T> specificClass;

    public FillAttributesDeserializer(Class<T> aClass) {
        super(aClass);
        this.specificClass = aClass;
    }

    public void deserialize(T element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        element.setFill(DeserializerParser.parseString("fill", jsonObject));
        element.setFillOpacity(DeserializerParser.parseString("fillOpacity", jsonObject));
    }


    @Override
    public T deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        final JsonNode jsonObject = jsonParser.getCodec().readTree(jsonParser);

        try {
            ElementType type;

            try {
                type = ElementType.fromString(jsonObject.get("elementType").asText());
                if (type == null) {
                    type = ElementType.ELEMENT_ATTRIBUTES;
                }
            } catch (NullPointerException e) {
                type = ElementType.ELEMENT_ATTRIBUTES;
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
