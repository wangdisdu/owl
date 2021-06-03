package com.owl.common;

import cn.hutool.core.io.IORuntimeException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class JsonUtil {
    public static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        MAPPER.registerModule(new SimpleModule()
                .addSerializer(Double.class, new JsonDoubleSerializer())
                .addSerializer(Float.class, new JsonFloatSerializer())
        );
    }

    public static String encode(Object value) throws IOException {
        return MAPPER.writeValueAsString(value);
    }

    public static String encodeUnSafe(Object value) {
        try {
            return encode(value);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    public static <T> T decode(String value, Class<T> valueType) throws IOException {
        return MAPPER.readValue(value, valueType);
    }

    public static <T> T decodeUnSafe(String value, Class<T> valueType) {
        try {
            return decode(value, valueType);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    public static <T> T decode(String value, TypeReference<T> valueTypeRef) throws IOException {
        return MAPPER.readValue(value, valueTypeRef);
    }

    public static <T> T decodeUnSafe(String value, TypeReference<T> valueTypeRef) {
        try {
            return decode(value, valueTypeRef);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    public static Map<String, Object> decode2Map(String value) throws IOException {
        return decode(value, new TypeReference<Map<String, Object>>() {});
    }

    public static Map<String, Object> decode2MapUnSafe(String value) {
        try {
            return decode2Map(value);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    public static JsonNode decode2Tree(String value) throws IOException {
        return MAPPER.readTree(value);
    }

    public static JsonNode decode2TreeUnSafe(String value) {
        try {
            return decode2Tree(value);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }
}
