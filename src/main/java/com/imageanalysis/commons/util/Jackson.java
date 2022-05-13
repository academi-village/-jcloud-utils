package com.imageanalysis.commons.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import lombok.var;

/**
 * Responsible to serialize/deserialize the objects into/from JSON.
 */
@RequiredArgsConstructor

public class Jackson {

    public static final ObjectMapper MAPPER = getObjectMapper();

    /**
     * The actual serializer/deserializer.
     */
    private final ObjectMapper mapper;

    public Jackson() {
        mapper = MAPPER;
    }

    private static ObjectMapper getObjectMapper() {
        return JsonMapper.builder()
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
                .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
                .enable(MapperFeature.AUTO_DETECT_FIELDS)
                .build()
                .registerModules(new Jdk8Module(), new JavaTimeModule())
                .setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL)
                .setDateFormat(new StdDateFormat())
                .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    /**
     * Deserialize the provided json string to the given type.
     *
     * @param json Represents the JSON string
     * @param type Represents the type to which the JSON string should deserialize.
     * @return The de-serialized object of the given type.
     */
    @SneakyThrows
    public <T> T fromJson(String json, Class<T> type) {
        return mapper.readValue(json, type);
    }

    /**
     * Converts the payment facilitator details field to PaymentFacilitatorDto.
     *
     * @param json json string
     * @param type a type that the json string should deserialize to it.
     * @return The de-serialized object of the given type.
     */
    @SneakyThrows
    public <T> T fromJson(String json, TypeReference<T> type) {
        return mapper.readValue(json, type);
    }

    /**
     * Deserialize the provided json string to the given type.
     *
     * @param json Represents the JSON in byte array format.
     * @param type Represents the type to which the JSON string should deserialize.
     * @return The de-serialized object of the given type.
     */
    @SneakyThrows
    public <T> T fromJson(byte[] json, Class<T> type) {
        return mapper.readValue(json, type);
    }

    /**
     * Deserialize the requested node from json to the given type.
     *
     * @param json      Represents the JSON string
     * @param fieldPath The path of requested field in json.
     * @param type      Represents the type to which the JSON string should deserialize.
     * @return The de-serialized object of the given type.
     */
    @SneakyThrows
    public <T> T fromJson(byte[] json, String fieldPath, Class<T> type) {
        val node = readPath(json, fieldPath);
        return mapper.treeToValue(node, type);
    }

    /**
     * Reads the provided field path from JSON string.
     *
     * @param json      Represents the JSON string
     * @param fieldPath The path of requested field in json.
     * @return The {@link JsonNode} of requested {@code fieldPath}.
     * @see JsonNode#path(String)
     */
    @SneakyThrows
    public JsonNode readPath(byte[] json, String fieldPath) {
        var node = mapper.readTree(json);
        for (String field : fieldPath.split("\\."))
            node = node.get(field);
        return node;
    }

    /**
     * Deserialize the provided JSON string to a {@link JsonNode}.
     *
     * @param json Represents the JSON string
     * @return The {@link JsonNode} of requested {@code fieldPath}.
     * @see JsonNode#path(String)
     */
    @SneakyThrows
    public JsonNode readTree(String json) {
        return mapper.readTree(json);
    }

    /**
     * Serializes the provided object to json string.
     *
     * @param object An object to be serialized into JSON string.
     * @return The JSON string of the given object.
     */
    @SneakyThrows
    public String toJson(Object object) {
        return mapper.writeValueAsString(object);
    }

    /**
     * Serializes the provided object to json byte array.
     *
     * @param object An object to be serialized into JSON byte array.
     * @return The JSON byte array of the given object.
     */
    @SneakyThrows
    public byte[] toJsonBytes(Object object) {
        return mapper.writeValueAsBytes(object);
    }

    /**
     * @see ObjectMapper#convertValue(Object, Class)
     */
    public <T> T convertValue(Object fromValue, Class<T> toValueType) {
        return mapper.convertValue(fromValue, toValueType);
    }

    /**
     * @see ObjectMapper#convertValue(Object, Class)
     */
    public <T> T convertValue(Object fromValue, TypeReference<T> toValueType) {
        return mapper.convertValue(fromValue, toValueType);
    }
}
