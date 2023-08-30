package com.biit.infographic.core.models.svg.serialization;

import com.biit.infographic.core.models.svg.ElementAttributes;
import com.biit.infographic.core.models.svg.ElementStroke;
import com.biit.infographic.core.models.svg.ElementType;
import com.biit.infographic.core.models.svg.SvgElement;
import com.biit.infographic.logger.InfographicEngineLogger;
import com.fasterxml.jackson.core.JacksonException;
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
        element.setElementAttributes(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("attributes").toPrettyString(), ElementAttributes.class));
        element.setElementStroke(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("stroke").toPrettyString(), ElementStroke.class));
    }


    @Override
    public T deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        final JsonNode jsonObject = jsonParser.getCodec().readTree(jsonParser);

        try {
            final ElementType type = ElementType.fromString(jsonObject.get("type").asText());
            if (type == null) {
                return null;
            }

            final T element = (T) type.getRelatedClass().getDeclaredConstructor().newInstance();
            deserialize(element, jsonObject, deserializationContext);
            return element;
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException
                 | NullPointerException e) {
            InfographicEngineLogger.severe(this.getClass().getName(), "Invalid node:\n" + jsonObject.toPrettyString());
            InfographicEngineLogger.errorMessage(this.getClass().getName(), e);
            throw new RuntimeException(e);
        }
    }
}
