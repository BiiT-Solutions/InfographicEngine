package com.biit.infographic.core.models.svg.serialization;

import com.biit.infographic.core.models.svg.ElementType;
import com.biit.infographic.core.models.svg.components.path.PathElement;
import com.biit.infographic.core.models.svg.exceptions.InvalidAttributeException;
import com.biit.infographic.logger.InfographicEngineLogger;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public abstract class PathElementDeserializer<T extends PathElement> extends StdDeserializer<T> {

    public PathElementDeserializer(Class<T> vc) {
        super(vc);
    }

    public void deserialize(T element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        element.setRelativeCoordinates(DeserializerParser.parseBoolean("relativeCoordinates", jsonObject));
    }


    @Override
    public T deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
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
            InfographicEngineLogger.severe(this.getClass(), "Invalid path node:\n" + jsonObject.toPrettyString());
            InfographicEngineLogger.errorMessage(this.getClass(), e);
            throw new InvalidAttributeException(this.getClass(), e);
        }
    }
}
