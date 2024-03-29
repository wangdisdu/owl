package com.owl.integration.elasticsearch.client;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class IndexMapping {
    private final String index;
    private final Map<String, String> mapping;

    public IndexMapping(String index, Map<String, String> mapping) {
        this.index = index;
        this.mapping = mapping;
    }

    public String getIndex() {
        return index;
    }

    public Map<String, String> getMapping() {
        return mapping;
    }

    public static IndexMapping fromJson(EsVersion version, String index, JsonNode json) {
        Iterator<JsonNode> iterator = json.iterator();
        Map<String, String> result = new LinkedHashMap<>();
        while (iterator.hasNext()) {
            JsonNode root = iterator.next();
            JsonNode mappings =  root.get("mappings");
            if (mappings == null) {
                continue;
            }
            if (version.gte(EsVersion.V7_0_0)) {
                JsonNode properties = mappings.get("properties");
                if (properties == null) {
                    continue;
                }
                flattenMappingProperty(null, properties, result);
            } else {
                for (JsonNode mapping : mappings) {
                    JsonNode properties = mapping.get("properties");
                    if (properties == null) {
                        continue;
                    }
                    flattenMappingProperty(null, properties, result);
                }
            }
        }
        return new IndexMapping(index, result);
    }

    private static void flattenMappingProperty(String parent, JsonNode node, Map<String, String> result) {
        Iterator<Map.Entry<String, JsonNode>> iterator = node.fields();
        while (iterator.hasNext()) {
            Map.Entry<String, JsonNode> properties = iterator.next();
            String key = properties.getKey();
            String name = parent == null ? key : parent + "." + key;
            JsonNode property = properties.getValue();
            if (property.has("properties")) {
                flattenMappingProperty(name, property.get("properties"), result);
            } else {
                result.putIfAbsent(name, property.get("type").asText());
            }
        }
    }
}
