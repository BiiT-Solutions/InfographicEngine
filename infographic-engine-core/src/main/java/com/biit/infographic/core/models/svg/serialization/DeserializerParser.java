package com.biit.infographic.core.models.svg.serialization;

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class DeserializerParser {
    public static final String TIMESTAMP_FORMAT = "dd-MM-yyyy HH:mm:ss";
    public static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern(TIMESTAMP_FORMAT);

    private DeserializerParser() {

    }

    public static String parseString(String name, JsonNode jsonObject) throws JsonProcessingException {
        if (jsonObject != null && jsonObject.get(name) != null) {
            return jsonObject.get(name).textValue();
        }
        return null;
    }

    public static Timestamp parseTimestamp(String name, JsonNode jsonObject) throws JsonProcessingException {
        if (jsonObject != null && jsonObject.get(name) != null) {
            final String value = jsonObject.get(name).asText();
            try {
                return Timestamp.valueOf(value);
            } catch (Exception e) {
                try {
                    return Timestamp.valueOf(LocalDateTime.from(TIMESTAMP_FORMATTER.parse(value)));
                } catch (Exception e3) {
                    return new Timestamp(new Date().getTime());
                }
            }
        }
        return null;
    }

    public static Long parseLong(String name, JsonNode jsonObject) throws JsonProcessingException {
        if (jsonObject != null && jsonObject.get(name) != null) {
            return jsonObject.get(name).longValue();
        }
        return null;
    }

    public static Integer parseInteger(String name, JsonNode jsonObject) throws JsonProcessingException {
        if (jsonObject != null && jsonObject.get(name) != null) {
            return jsonObject.get(name).intValue();
        }
        return null;
    }

    public static Double parseDouble(String name, JsonNode jsonObject) throws JsonProcessingException {
        if (jsonObject != null && jsonObject.get(name) != null) {
            return jsonObject.get(name).doubleValue();
        }
        return null;
    }

    public static <T> List<T> parseList(String name, JsonNode jsonObject) throws JsonProcessingException {
        final JsonNode valuesJson = jsonObject.get(name);
        if (valuesJson != null) {
            return ObjectMapperFactory.getObjectMapper().readValue(valuesJson.toPrettyString(),
                    new TypeReference<>() {
                    });
        }
        return new ArrayList<>();
    }

    public static Map<String, String> parseMap(String name, JsonNode jsonObject) throws JsonProcessingException {
        final JsonNode valuesJson = jsonObject.get(name);
        if (valuesJson != null) {
            return ObjectMapperFactory.getObjectMapper().readValue(valuesJson.toPrettyString(),
                    new TypeReference<HashMap<String, String>>() {
                    });
        }
        return new HashMap<>();
    }

    public static boolean parseBoolean(String name, JsonNode jsonObject) throws JsonProcessingException {
        if (jsonObject != null && jsonObject.get(name) != null) {
            return jsonObject.get(name).booleanValue();
        }
        return false;
    }
}
