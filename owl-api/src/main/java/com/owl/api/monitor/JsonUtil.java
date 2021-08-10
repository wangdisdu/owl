package com.owl.api.monitor;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.JsonNode;

import java.lang.reflect.Field;

public class JsonUtil {
    public static <T> T decode(JsonNode fromJson, T toObject) {
        Class<?> clazz = toObject.getClass();
        Field[] fields = ReflectUtil.getFields(clazz);
        for (Field field : fields) {
            Json annotation = field.getAnnotation(Json.class);
            if (annotation == null
                    || (annotation.path().length == 0
                    && annotation.path2().length == 0)) {
                continue;
            }
            JsonNode node = path(fromJson, annotation.path());
            if (node == null) {
                node = path(fromJson, annotation.path2());
            }
            if (node == null) {
                if (annotation.required()) {
                    String path = StrUtil.join(".", annotation.path())
                            + "; " + StrUtil.join(".", annotation.path2());
                    throw new RuntimeException("invalid json path " + path);
                }
                continue;
            }
            field.setAccessible(true);
            if (field.getType() == long.class) {
                ReflectUtil.setFieldValue(toObject, field, node.asLong());
            } else if (field.getType() == int.class) {
                ReflectUtil.setFieldValue(toObject, field, node.asInt());
            } else if (field.getType() == double.class) {
                ReflectUtil.setFieldValue(toObject, field, node.asDouble());
            } else if (field.getType() == float.class) {
                ReflectUtil.setFieldValue(toObject, field, new Double(node.asDouble()).floatValue());
            } else if (field.getType() == String.class) {
                ReflectUtil.setFieldValue(toObject, field, node.asText());
            }
        }
        return toObject;
    }

    public static JsonNode path(JsonNode json, String[] path) {
        if (path.length == 0) {
            return null;
        }
        JsonNode node = json;
        for (String p : path) {
            if (node == null) {
                return null;
            }
            node = node.get(p);
        }
        return node;
    }
}
