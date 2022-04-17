package com.github.academivillage.jcloud.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import lombok.var;

import static com.github.academivillage.jcloud.util.Jackson.OBJECT_MAPPER;

/**
 * Responsible to serialize/deserialize the objects into/from JSON.
 */
@RequiredArgsConstructor
public class Serializer {

    /**
     * The actual serializer/deserializer.
     */
    private final ObjectMapper objectMapper;

    public static Serializer newInstance() {
        return new Serializer(OBJECT_MAPPER);
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
        return objectMapper.readValue(json, type);
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
        return objectMapper.readValue(json, type);
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
        return objectMapper.readValue(json, type);
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
        return objectMapper.treeToValue(node, type);
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
        var node = objectMapper.readTree(json);
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
        return objectMapper.readTree(json);
    }

    /**
     * Serializes the provided object to json string.
     *
     * @param object An object to be serialized into JSON string.
     * @return The JSON string of the given object.
     */
    @SneakyThrows
    public String toJson(Object object) {
        return objectMapper.writeValueAsString(object);
    }

    /**
     * Serializes the provided object to json byte array.
     *
     * @param object An object to be serialized into JSON byte array.
     * @return The JSON byte array of the given object.
     */
    @SneakyThrows
    public byte[] toJsonBytes(Object object) {
        return objectMapper.writeValueAsBytes(object);
    }
}
